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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bboxdb.commons.math.Hyperrectangle;
import org.junit.Assert;

import com.github.jnidzwetzki.spatialindex.SpatialIndexBuilder;
import com.github.jnidzwetzki.spatialindex.SpatialIndexEntry;
import com.github.jnidzwetzki.spatialindex.SpatialIndexException;
import com.github.jnidzwetzki.spatialindex.SpatialIndexReader;

public class RTreeTestHelper {

	/**
	 * Test the query
	 * 
	 * @param entries
	 * @param index
	 * @throws StorageManagerException 
	 */
	public static void queryIndex(final List<SpatialIndexEntry> entries, final SpatialIndexReader index) throws SpatialIndexException {
		
		for(final SpatialIndexEntry entry: entries) {
			final List<? extends SpatialIndexEntry> resultList = index.getEntriesForRegion(entry.getHyperrectangle());
			Assert.assertTrue(resultList.size() >= 1);
			
			final List<Integer> keyResult = resultList
					.stream()
					.map(e -> e.getValue())
					.filter(k -> k.equals(entry.getValue()))
					.collect(Collectors.toList());

			Assert.assertTrue("Searching for: " + entry, keyResult.size() == 1);
		}
	}

	/**
	 * Test the query
	 * 
	 * @param entries
	 * @param index
	 */
	public static void queryIndex(final List<SpatialIndexEntry> entries, final SpatialIndexBuilder index) {
		
		for(final SpatialIndexEntry entry: entries) {
			final List<? extends SpatialIndexEntry> resultList = index.getEntriesForRegion(entry.getHyperrectangle());
			Assert.assertTrue(resultList.size() >= 1);
			
			final List<Integer> keyResult = resultList
					.stream()
					.map(e -> e.getValue())
					.filter(k -> k.equals(entry.getValue()))
					.collect(Collectors.toList());

			Assert.assertTrue("Searching for: " + entry, keyResult.size() == 1);
		}
	}

	/**
	 * Generate a list of tuples
	 * @return
	 */
	public static List<SpatialIndexEntry> getEntryList() {
		final List<SpatialIndexEntry> entryList = new ArrayList<SpatialIndexEntry>();
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(0d, 1d, 0d, 1d), 1));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(1d, 2d, 1d, 3d), 2));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(2d, 3d, 0d, 1d), 3));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(3d, 4d, 3d, 7d), 4));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(1.2d, 2.2d, 0d, 1d), 5));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(4.6d, 5.6d, 0d, 1d), 6));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(5.2d, 6.2d, 4d, 5d), 7));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(5.1d, 6.1d, 0d, 1d), 8));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(6.1d, 7.1d, 0d, 1d), 9));
		entryList.add(new SpatialIndexEntry(new Hyperrectangle(8.1d, 9.1d, 2d, 5d), 10));
		return entryList;
	}
	
	/**
	 * Generate some random tuples
	 * @return
	 */
	public static List<SpatialIndexEntry> generateRandomTupleList(final int dimensions) {
		final List<SpatialIndexEntry> entryList = new ArrayList<SpatialIndexEntry>();
		final Random random = new Random();
		
		for(int i = 0; i < 5000; i++) {
			final double[] HyperrectangleData = new double[dimensions * 2];
			
			for(int d = 0; d < dimensions; d++) {
				final double begin = random.nextInt() % 1000;
				final double extent = Math.abs(random.nextInt() % 1000);
				HyperrectangleData[2 * d] = begin;            // Start coordinate
				HyperrectangleData[2 * d + 1] = begin+extent; // End coordinate
			}
			
			final SpatialIndexEntry entry = new SpatialIndexEntry(new Hyperrectangle(HyperrectangleData), i);
			entryList.add(entry);
		}

		return entryList;
	}
}
