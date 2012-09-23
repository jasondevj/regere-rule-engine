/*
 * Copyright 2012 The regere-rule-engine Project
 *
 * The regere-rule-engine Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package dev.j.regere.respository;

import dev.j.regere.domain.RegereRuleFlowWrapper;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * $LastChangedDate$
 * $LastChangedBy$
 * $LastChangedRevision$
 */
public class FileIntermediatePersistedTable implements IntermediatePersistedTable {
    private ByteBuffer buffer;

    @Override
    public void init() {
        File dateFile = new File("intermediate_table.data");
        File indexFile = new File("intermediate_table.index");

        // Create a read-only memory-mapped file
//        FileChannel roChannel = new RandomAccessFile(file, "r").getChannel();
//        ByteBuffer roBuf = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int)roChannel.size());

        // Create a read-write memory-mapped file
//        FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
//        ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, (int)rwChannel.size());
//
//        wrBuf.

        // Create a private (copy-on-write) memory-mapped file.
        // Any write to this channel results in a private copy of the data.
//        FileChannel pvChannel = new RandomAccessFile(file, "rw").getChannel();
//        ByteBuffer pvBuf = roChannel.map(FileChannel.MapMode.READ_WRITE, 0, (int)rwChannel.size());
    }

    @Override
    public RegereRuleFlowWrapper load(String regereId, String commonIdentifier, Map<String, Object> currentEvent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void persistEvent(String regereId, String commonIdentifier, RegereRuleFlowWrapper flowWrapper, List<String> persitableKeys) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
