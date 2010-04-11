package org.apache.fulcrum.security;

/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;

/**
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @version $Id: StartingSecurityServicesTest.java,v 1.1 2005/11/25 08:50:14 bgidley Exp $
 */

public class StartingSecurityServicesTest extends TestCase {
    public void setUp() throws Exception {
        // Force Registry to have test configuration
        Resource resource = new ClasspathResource(new DefaultClassResolver(), "META-INF/hivemodule_test.xml");
        RegistryManager.getInstance().getResources().add(resource);
        super.setUp();
    }

    public void testStartingHibernateSecurity() throws Exception {
        SecurityService securityService = (SecurityService) RegistryManager.getInstance().getRegistry().getService(
                "fulcrum.security.hivemind.securityServiceDynamic", SecurityService.class);

        assertNotNull(securityService);
        assertNotNull(securityService.getGroupManager());
        assertNotNull(securityService.getModelManager());
        assertNotNull(securityService.getPermissionManager());
        assertNotNull(securityService.getUserManager());
        assertNotNull(securityService.getRoleManager());
    }

}
