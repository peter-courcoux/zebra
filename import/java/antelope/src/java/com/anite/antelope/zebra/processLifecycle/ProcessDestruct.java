/*
 * Copyright 2004 Anite - Central Government Division
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anite.antelope.zebra.processLifecycle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.antelope.zebra.helper.ZebraHelper;
import com.anite.antelope.zebra.om.AntelopeProcessDefinition;
import com.anite.antelope.zebra.om.AntelopeProcessInstance;
import com.anite.antelope.zebra.om.AntelopeTaskDefinition;
import com.anite.antelope.zebra.om.AntelopeTaskInstance;
import com.anite.zebra.core.api.IProcessDestruct;
import com.anite.zebra.core.exceptions.DestructException;
import com.anite.zebra.core.factory.api.IStateFactory;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.core.state.api.ITransaction;
import com.anite.zebra.ext.definitions.api.IProperties;

/**
 * Subprocess Aware Process Destruction
 * 
 * @author Matthew Norris
 * @author Ben Gidley
 *  
 */
public class ProcessDestruct implements IProcessDestruct {

	private static Logger log = LoggerFactory.getLogger(ProcessDestruct.class);

	public void processDestruct(IProcessInstance processInstance)
			throws DestructException {
		if (log.isInfoEnabled()) {
			log.info("processDestruct called for InterfaceProcessInstance "
					+ processInstance.getProcessInstanceId());
		}

		try {
			AntelopeProcessInstance antelopeProcessInstance = (AntelopeProcessInstance) processInstance;
			processDestruct(antelopeProcessInstance);
		} catch (Exception e) {
			log.error(e);
			throw new DestructException(e);
		}
	}

	/**
	 * Destroy process if this is the end of the subflow tell the engine it can
	 * move on
	 * 
	 * @param processInstance
	 * @throws DestructException
	 */
	public void processDestruct(AntelopeProcessInstance processInstance)
			throws DestructException {
		try {

			if (processInstance.getParentTaskInstance() != null) {
				if (log.isInfoEnabled()) {
					log.info("Parent Task for this process found");
				}

				IStateFactory stateFactory = ZebraHelper.getInstance()
						.getStateFactory();
				ITransaction t = stateFactory.beginTransaction();
				AntelopeTaskInstance parentTaskInstance = (AntelopeTaskInstance) processInstance
						.getParentTaskInstance();
				mapProcessOutputs(processInstance, parentTaskInstance);
				stateFactory.saveObject(processInstance
						.getParentProcessInstance());
				parentTaskInstance.setState(
						ITaskInstance.STATE_AWAITINGCOMPLETE);

				stateFactory
						.saveObject(parentTaskInstance);

				// Need to do this - as process instances never get deleted
				// but tasks do.
				processInstance.setParentTaskInstance(null);
				stateFactory.saveObject(processInstance);

				t.commit();

				if (log.isInfoEnabled()) {
					log
							.info("Output mapping complete, attempting to transition the Parent Task");
				}
				ZebraHelper.getInstance().getEngine().transitionTask(
						parentTaskInstance);

			}
		} catch (Exception e) {
			String emsg = "Failed to map outputs to source task for process "
					+ processInstance.getProcessInstanceId();
			log.error(emsg, e);
			throw new DestructException(emsg, e);
		}
	}

	/**
	 * maps the outputs of the processinstance to the outputs of the
	 * taskinstance
	 * 
	 * If the push outputs flag is set ALL properties will be copied which is
	 * messy so only use it if you are to lazy to specificy the outputs (btw any
	 * developers working on projects for Anite that use it will be mocked
	 * cruelly)
	 * 
	 * @param processInstance
	 * @param taskInstance
	 */
	private void mapProcessOutputs(AntelopeProcessInstance processInstance,
			AntelopeTaskInstance parentTaskInstance) throws Exception {

	    if (log.isInfoEnabled()) {
			log.info("Attempting to map Outputs from \""
					+ ((AntelopeProcessDefinition) (processInstance
							.getProcessDef())).getDisplayName() + "\" to \""
					+ parentTaskInstance.getCaption() + "\"");
		}

		boolean pushOutputs = ((AntelopeTaskDefinition) parentTaskInstance
				.getTaskDefinition()).getPushOutputs();

		Map parentProperties = ((AntelopeProcessInstance) parentTaskInstance
				.getProcessInstance()).getPropertySet();

		Map childProperties = processInstance.getPropertySet();

		Set keys = new HashSet();

		if (pushOutputs) {
			keys.addAll(childProperties.keySet());
		} else {
			IProperties outputs = ((AntelopeTaskDefinition) parentTaskInstance
					.getTaskDefinition()).getOutputs();
			Iterator outputKeys = outputs.keys();
			while (outputKeys.hasNext()) {
				keys.add(outputKeys.next());
			}
		}

		Iterator keysToMap = keys.iterator();
		while (keysToMap.hasNext()) {
			Object key = keysToMap.next();
			parentProperties.put(key, childProperties.get(key));
		}

	}
}