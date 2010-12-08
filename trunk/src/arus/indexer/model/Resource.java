package arus.indexer.model;

import java.io.BufferedWriter;
import java.util.Vector;

/**
 * Resource class represents each and every resource of a software to be indexed.
 * @author chandu
 *
 */
public class Resource {
	private String filename;

	/**
	 * 
	 * @return 
	 */
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private String version;
	private boolean isDir = false;
	private Vector<Resource> resoucesList;

	public Resource(boolean isDir) {
		this.isDir = isDir;
		resoucesList = new Vector<Resource>();
	}

	public boolean isDir() {
		return isDir;
	}

	public void addResource(Resource rsc) {
		if (this.isDir) {
			resoucesList.add(rsc);
		}
	}

	public Resource[] getResourceList() {
		Resource[] t = new Resource[this.resoucesList.size()];
 		return this.resoucesList.toArray(t);
	}

	public void writeXML(BufferedWriter writer) throws Exception
	{
		if(isDir())
		{			
			writer.write("<dir fname=\""+filename+"\" version=\""+version+"\">\n");
			for(Resource i: getResourceList())
			{
				i.writeXML(writer);				
			}
			writer.write("</dir>\n");
		}
		else
		{
//			System.out.println(filename);
			String name = filename;
			String extension = "";
			if(filename.indexOf('.') != -1)
			{
				name = name.substring( 0, filename.indexOf('.') );
				extension = filename.substring( filename.indexOf('.')+1 );
			}
			writer.write("<file fname=\""+name+"\" extension=\""+extension+"\" version=\""+version+"\" />\n");
			
		}		
	}

	
	// print all the file names and directory names of this resource.
	// if the resource has folder type resources, call toString on them.
	public String toString() {
		String value = "";
		return value;
	}
}