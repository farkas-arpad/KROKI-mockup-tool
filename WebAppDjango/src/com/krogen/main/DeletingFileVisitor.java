package com.krogen.main;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.log4j.Logger;
 
/**
 * File visitor for deleting files recursively 
 * @author N
 *
 */
public class DeletingFileVisitor extends SimpleFileVisitor<Path>{
 
	static Logger log = Logger.getLogger(DeletingFileVisitor.class.getName());
	
	
@Override
public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
        throws IOException {
    if(attributes.isRegularFile()){
    	log.info("Deleting file: " + file.getFileName());
        Files.delete(file);
    }
    return FileVisitResult.CONTINUE;
}
 
@Override
    public FileVisitResult postVisitDirectory(Path directory, IOException ioe)
            throws IOException {
        log.info("Deleting Directory: " + directory.getFileName());        
        Files.delete(directory);
        return FileVisitResult.CONTINUE;
    }
 
@Override
    public FileVisitResult visitFileFailed(Path file, IOException ioe)
            throws IOException {
        log.error("Something went wrong while working on : " + file.getFileName(),ioe);               
      //  ioe.printStackTrace();
        return FileVisitResult.CONTINUE;
    }
}