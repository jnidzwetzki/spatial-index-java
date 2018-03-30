# Spatial indexing algorithms for java (sia4j)

Implementation of spatial indexing algorithms in java. At the moment, only an [r-tree](https://en.wikipedia.org/wiki/R-tree) index is implemented by this project.

<a href="https://travis-ci.org/jnidzwetzki/crypto-bot">
  <img alt="Build Status" src="https://travis-ci.org/jnidzwetzki/spatial-index-java.svg?branch=master">
</a><a href="http://makeapullrequest.com">
 <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" />
</a><a href="https://codecov.io/gh/jnidzwetzki/spatial-index-java">
  <img src="https://codecov.io/gh/jnidzwetzki/spatial-index-java/branch/master/graph/badge.svg" />
</a>

## Features of the R-tree implementation
* Supports rectangle geometries
* Supports n-dimensional data
* Support serializing to file
* Can be used as in-memory data structure
* R-Tree can be serialized and accessed via _memory mapped io_. This is usefull for very large datasets.

## Examples

### Building the r-tree and execute a rane query
```java
// Two entries with a two-dimensional bounding box
final SpatialIndexEntry entry1 = new SpatialIndexEntry(new BoundingBox(1d, 2d, 1d, 2d), "abc");
final SpatialIndexEntry entry2 = new SpatialIndexEntry(new BoundingBox(10d, 20d, 10d, 20d), "def");

final SpatialIndexBuilder index = new RTreeBuilder();
index.bulkInsert(Arrays.asList(entry1, entry2);

// Query data
final List<? extends SpatialIndexEntry> resultList = index.getEntriesForRegion(new BoundingBox(1d, 1.5d, 1d, 1.5d));
```

### Write the r-tree into a file and read into memory
```java
// Write and read to file
final File tempFile = File.createTempFile("rtree-", "-test");
final RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");		
index.writeToFile(raf);
raf.close();

final AbstractRTreeReader indexRead = new RTreeMemoryReader();
final RandomAccessFile rafRead = new RandomAccessFile(tempFile, "r");
indexRead.readFromFile(rafRead);
rafRead.close();
```

### Write the r-tree into a file and access the file via memory mapped io
```java
// Write and read to file
final File tempFile = File.createTempFile("rtree-", "-test");
final RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");		
index.writeToFile(raf);
raf.close();

final AbstractRTreeReader indexRead = new RTreeMMFReader();
final RandomAccessFile rafRead = new RandomAccessFile(tempFile, "r");
indexRead.readFromFile(rafRead);
rafRead.close();
```