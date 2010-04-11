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

package com.anite.antelope.modules.actions.myFirstWorkflow;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

import com.anite.antelope.modules.screens.myFirstWorkflow.EnterExpiryDate;
import com.anite.antelope.zebra.modules.actions.BaseWorkflowAction;
import com.anite.antelope.zebra.om.AntelopeProcessInstance;
import com.anite.antelope.zebra.om.AntelopePropertySetEntry;
import com.anite.antelope.zebra.om.AntelopeTaskInstance;
import com.anite.penguin.form.Field;
import com.anite.penguin.modules.tools.FormTool;

/**
 * @author John.Rae
 */
public class EnterExpiryDateAction extends BaseWorkflowAction {


	/**
	 * Form has been submitted and passed validation
	 */
	protected boolean doPerform(RunData runData, Context context,
			AntelopeTaskInstance taskInstance,
			AntelopeProcessInstance processInstance, FormTool tool)
			throws Exception {

		// Get the validated date 
		Field expiry = (Field) tool.getFields().get(EnterExpiryDate.EXPIRY);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");
		Date expiryDate = new Date(sdf.parse(expiry.getValue()).getTime());

		// Save the age in the processInstance
		// Obviously in a real app this could be save it in the database
		AntelopePropertySetEntry expiryEntry = new AntelopePropertySetEntry();
		expiryEntry.setObject(expiryDate);
		processInstance.getPropertySet().put(
			EnterExpiryDate.EXPIRY, expiryEntry);

		return true;
		
	}

	/**
	 * This is set to true as we don't want doPerform to be
	 * called if validation fails.
	 */
	protected boolean enforceValidation() {

		return true;
	}

}