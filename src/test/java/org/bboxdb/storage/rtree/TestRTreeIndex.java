/*******************************************************************************
 *
 *    Copyright (C) 2015-2018 the BBoxDB project
 *  
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License. 
 *    
 *******************************************************************************/
package org.bboxdb.storage.rtree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.bboxdb.commons.math.Hyperrectangle;
import org.junit.Assert;
import org.junit.Test;

import com.github.jnidzwetzki.spatialindex.Const;
import com.github.jnidzwetzki.spatialindex.SpatialIndexBuilder;
import com.github.jnidzwetzki.spatialindex.SpatialIndexEntry;
import com.github.jnidzwetzki.spatialindex.rtree.RTreeBuilder;

public class TestRTreeIndex {
	
	/**
	 * Test to insert and to read the bounding boxes
	 */
	@Test(timeout=60000)
	public void testBoxesInsert() {
		final List<SpatialIndexEntry> elements = RTreeTestHelper.getEntryList();
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(elements);
	}
	
	@Test(timeout=60000)
	public void testQueryOnEmptytree() {
		final SpatialIndexBuilder index = new RTreeBuilder();
		final List<? extends SpatialIndexEntry> result = index.getEntriesForRegion(new Hyperrectangle(1d, 1d, 2d, 2d));
		Assert.assertTrue(result.isEmpty());
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery1d0() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.getEntryList();
		
		final SpatialIndexBuilder index = new RTreeBuilder(4);
		index.bulkInsert(tupleList);
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery1d() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.getEntryList();
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery2d() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.generateRandomTupleList(2);
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery3d() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.generateRandomTupleList(3);
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery4d() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.generateRandomTupleList(4);
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery5d() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.generateRandomTupleList(5);
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	/**
	 * Test to query the index
	 */
	@Test(timeout=60000)
	public void testBoxQuery10d() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.generateRandomTupleList(10);
		
		final SpatialIndexBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		
		RTreeTestHelper.queryIndex(tupleList, index);
	}
	
	
	/**
	 * Test the covering of the nodes
	 */
	@Test(timeout=60000)
	public void testCovering() {
		final List<SpatialIndexEntry> tupleList = RTreeTestHelper.generateRandomTupleList(3);
		
		final RTreeBuilder index = new RTreeBuilder();
		index.bulkInsert(tupleList);
		
		index.testCovering();		
	}
	
	/**
	 * Test the decoding an encoding of an rtree entry
	 * @throws IOException 
	 */
	@Test(timeout=60000)
	public void testEncodeDecodeRTreeEntryFromFile() throws IOException {
		final Hyperrectangle Hyperrectangle = new Hyperrectangle(4.1, 8.1, 4.2, 8.8);
		
		final File tempFile = File.createTempFile("rtree-", "-test");
		tempFile.deleteOnExit();
		final RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");		
		final SpatialIndexEntry rTreeSpatialIndexEntry = new SpatialIndexEntry(Hyperrectangle, 1);
		rTreeSpatialIndexEntry.writeToFile(raf);		
		raf.close();
		
		final RandomAccessFile rafRead = new RandomAccessFile(tempFile, "r");
		final SpatialIndexEntry readEntry = SpatialIndexEntry.readFromFile(rafRead);
		rafRead.close();
		
		Assert.assertEquals(rTreeSpatialIndexEntry.getValue(), readEntry.getValue());
		Assert.assertEquals(rTreeSpatialIndexEntry.getHyperrectangle(), readEntry.getHyperrectangle());
	}
	
	/**
	 * Test the decoding an encoding of an rtree entry
	 * @throws IOException 
	 */
	@Test(timeout=60000)
	public void testEncodeDecodeRTreeEntryFromByteBuffer() throws IOException {
		final Hyperrectangle Hyperrectangle = new Hyperrectangle(4.1, 8.1, 4.2, 8.8);
		
		final File tempFile = File.createTempFile("rtree-", "-test");
		tempFile.deleteOnExit();
		final RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");		
		final SpatialIndexEntry rTreeSpatialIndexEntry = new SpatialIndexEntry(Hyperrectangle, 1);
		rTreeSpatialIndexEntry.writeToFile(raf);		
		raf.close();
		
		final Path path = Paths.get(tempFile.getAbsolutePath());
		final byte[] data = Files.readAllBytes(path);
		final ByteBuffer bb = ByteBuffer.wrap(data);
		bb.order(Const.APPLICATION_BYTE_ORDER);
		
		final SpatialIndexEntry readEntry = SpatialIndexEntry.readFromByteBuffer(bb);
		
		Assert.assertEquals(rTreeSpatialIndexEntry.getValue(), readEntry.getValue());
		Assert.assertEquals(rTreeSpatialIndexEntry.getHyperrectangle(), readEntry.getHyperrectangle());
	}

	/**
	 * Test the creation of a rtree with a invalid max node size
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testWrongNodeSize0() {
		new RTreeBuilder(0);
	}
	
	/**
	 * Test the creation of a rtree with a invalid max node size
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testWrongNodeSize1() {
		new RTreeBuilder(-1);
	}
	
	/**
	 * Test the bounding box of an empty r-tree
	 */
	@Test(timeout=60000)
	public void testEmptryRTreeBBox() {
		final RTreeBuilder index = new RTreeBuilder();
		final List<? extends SpatialIndexEntry> result = index.getEntriesForRegion(Hyperrectangle.FULL_SPACE);
		Assert.assertTrue(result.isEmpty());
	}
	
}
