/*

   Copyright 2001  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.flex.forks.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @version $Id$
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public class CmapFormat6 extends CmapFormat {

    private short format;
    private short length;
    private short version;
    private short firstCode;
    private short entryCount;
    private short[] glyphIdArray;

    protected CmapFormat6(RandomAccessFile raf) throws IOException {
        super(raf);
        format = 6;
    }

    public int getFirst() { return 0; }
    public int getLast()  { return 0; }
    
    public int mapCharCode(int charCode) {
        return 0;
    }
}
