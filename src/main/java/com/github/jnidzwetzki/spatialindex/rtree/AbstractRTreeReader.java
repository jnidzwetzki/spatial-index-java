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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.github.jnidzwetzki.spatialindex.Const;
import com.github.jnidzwetzki.spatialindex.SpatialIndexException;
import com.github.jnidzwetzki.spatialindex.SpatialIndexReader;

public abstract class AbstractRTreeReader implements SpatialIndexReader {

	/**
	 * The max size of a child node
	 */
	protected int maxNodeSize;
	
	/**
	 * Get the max node size for the index
	 * @return
	 */
	public int getMaxNodeSize() {
		return maxNodeSize;
	}
	
	/**
	 * Validate the magic bytes of a stream
	 * 
	 * @return a InputStream or null
	 * @throws StorageManagerException
	 * @throws IOException 
	 */
	protected void validateStream(final RandomAccessFile randomAccessFile) throws IOException, SpatialIndexException {
		
		// Validate file - read the magic from the beginning
		final byte[] magicBytes = new byte[Const.MAGIC_BYTES_SPATIAL_RTREE_INDEX.length];
		randomAccessFile.readFully(magicBytes, 0, Const.MAGIC_BYTES_SPATIAL_RTREE_INDEX.length);

		if(! Arrays.equals(magicBytes, Const.MAGIC_BYTES_SPATIAL_RTREE_INDEX)) {
			throw new SpatialIndexException("Spatial index file does not contain the magic bytes");
		}
	}
	
}
