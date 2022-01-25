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
package com.github.jnidzwetzki.spatialindex.rtree;

import java.util.List;

import org.bboxdb.commons.Pair;
import org.bboxdb.commons.math.Hyperrectangle;

import com.github.jnidzwetzki.spatialindex.HyperrectangleEntity;

public class QuadraticSeedPicker<T extends HyperrectangleEntity> {

	/**
	 * Find the seeds for the split
	 * @param insertedNode
	 * @return
	 */
	protected Pair<T, T> quadraticPickSeeds(final List<T> allEntries) {
		
		assert(allEntries.size() >= 2);
		
		double maxWaste = Double.MAX_VALUE;
		Pair<T, T> result = null;
		
		for(final T box1 : allEntries) {
			for(final T box2 : allEntries) {
				
				if(box1 == box2) {
					continue;
				}
				
				final Hyperrectangle Hyperrectangle1 = box1.getHyperrectangle();
				final Hyperrectangle Hyperrectangle2 = box2.getHyperrectangle();
				
				final double coveringArea 
					= Hyperrectangle.getCoveringBox(Hyperrectangle1, Hyperrectangle2).getVolume();
				
				final double waste = coveringArea - Hyperrectangle1.getVolume()
						- Hyperrectangle2.getVolume();
				
				if(waste < maxWaste) {
					result = new Pair<T,T>(box1, box2);
					maxWaste = waste;
				}
			}	
		}
		
		assert(result != null) : "Unable to find seeds";
		
		// Remove seeds from available objects
		allEntries.remove(result.getElement1());
		allEntries.remove(result.getElement2());
		
		return result;
	}
}
