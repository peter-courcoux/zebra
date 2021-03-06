package org.apache.fulcrum.security.acl;
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

import java.io.Serializable;


/**
 * This interface is a marker interface for AccessControlList.  Basically
 * allows us at somepoint to swap different ACL's under the covers with 
 * Avalon.  Actual ACL's are dependent on their model being used.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: AccessControlList.java,v 1.1 2005/11/14 18:20:49 bgidley Exp $
 */
public interface AccessControlList
  extends Serializable
{

   
   
}
