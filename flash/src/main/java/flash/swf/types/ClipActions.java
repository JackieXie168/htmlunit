/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package flash.swf.types;

import java.util.List;

/**
 * A value object for clip actions.
 *
 * @author Clement Wong
 */
public class ClipActions
{
    /**
     * All events used in these clip actions
     */
	public int allEventFlags;

    /**
     * Individual event handlers.  List of ClipActionRecord instances.
     */
	public List clipActionRecords;

    public boolean equals(Object object)
    {
        boolean isEqual = false;

        if (object instanceof ClipActions)
        {
            ClipActions clipActions = (ClipActions) object;

            if ( (clipActions.allEventFlags == this.allEventFlags) &&
                 ( ( (clipActions.clipActionRecords == null) && (this.clipActionRecords == null) ) ||
                   ( (clipActions.clipActionRecords != null) && (this.clipActionRecords != null) &&
                     ArrayLists.equals( clipActions.clipActionRecords,
                                    this.clipActionRecords ) ) ) )
            {
                isEqual = true;
            }
        }

        return isEqual;
    }
}
