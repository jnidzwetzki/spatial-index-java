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
package com.github.jnidzwetzki.spatialindex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.bboxdb.commons.io.DataEncoderHelper;
import org.bboxdb.commons.math.Hyperrectangle;

public class SpatialIndexEntry implements HyperrectangleEntity {

	/**
	 * The key
	 */
	protected final int value;
	
	/**
	 * The bounding box
	 */
	protected final Hyperrectangle hyperrectangle;

	public SpatialIndexEntry(final Hyperrectangle hyperrectangle, final int value) {
		this.value = value;
		this.hyperrectangle = hyperrectangle;
	}
	
	/**
	 * Get the value
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see com.github.jnidzwetzki.spatialindex.HyperrectangleEntity#getHyperrectangle()
	 */
	@Override
	public Hyperrectangle getHyperrectangle() {
		return hyperrectangle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hyperrectangle == null) ? 0 : hyperrectangle.hashCode());
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpatialIndexEntry other = (SpatialIndexEntry) obj;
		if (hyperrectangle == null) {
			if (other.hyperrectangle != null)
				return false;
		} else if (!hyperrectangle.equals(other.hyperrectangle))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SpatialIndexEntry [value=" + value + ", Hyperrectangle=" + hyperrectangle + "]";
	}
	
	/**
	 * Write the node to a stream
	 * @param randomAccessFile
	 * @throws IOException
	 */
	public void writeToFile(final RandomAccessFile randomAccessFile) throws IOException {
		final ByteBuffer keyBytes = DataEncoderHelper.intToByteBuffer(value);
		randomAccessFile.write(keyBytes.array());

		final byte[] HyperrectangleBytes = hyperrectangle.toByteArray();		
		final ByteBuffer boxLengthBytes = DataEncoderHelper.intToByteBuffer(HyperrectangleBytes.length);
		
		randomAccessFile.write(boxLengthBytes.array());
		randomAccessFile.write(HyperrectangleBytes);
	}
	
	/**
	 * Read the node from a stream
	 * @param inputStream
	 * @return
	 * @throws IOException 
	 */
	public static SpatialIndexEntry readFromFile(final RandomAccessFile randomAccessFile) throws IOException {

		final byte[] keyBytes = new byte[DataEncoderHelper.INT_BYTES];
		final byte[] boxLengthBytes = new byte[DataEncoderHelper.INT_BYTES];
		
		randomAccessFile.readFully(keyBytes, 0, keyBytes.length);
		randomAccessFile.readFully(boxLengthBytes, 0, boxLengthBytes.length);

		final int key = DataEncoderHelper.readIntFromByte(keyBytes);
		final int bboxLength = DataEncoderHelper.readIntFromByte(boxLengthBytes);

		final byte[] bboxBytes = new byte[bboxLength];		
		randomAccessFile.readFully(bboxBytes, 0, bboxBytes.length);

		final Hyperrectangle hyperrectangle = Hyperrectangle.fromByteArray(bboxBytes);
	
		return new SpatialIndexEntry(hyperrectangle, key);
	}
	
	/**
	 * Read the node from a byte buffer
	 * @param inputStream
	 * @return
	 * @throws IOException 
	 */
	public static SpatialIndexEntry readFromByteBuffer(final ByteBuffer buffer) throws IOException {
		final byte[] keyBytes = new byte[DataEncoderHelper.INT_BYTES];
		final byte[] boxLengthBytes = new byte[DataEncoderHelper.INT_BYTES];
		
		buffer.get(keyBytes, 0, keyBytes.length);
		buffer.get(boxLengthBytes, 0, boxLengthBytes.length);

		final int key = DataEncoderHelper.readIntFromByte(keyBytes);
		final int bboxLength = DataEncoderHelper.readIntFromByte(boxLengthBytes);

		final byte[] bboxBytes = new byte[bboxLength];		
		buffer.get(bboxBytes, 0, bboxBytes.length);

		final Hyperrectangle hyperrectangle = Hyperrectangle.fromByteArray(bboxBytes);
	
		return new SpatialIndexEntry(hyperrectangle, key);
	}

}
