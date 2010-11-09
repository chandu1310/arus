package arus.indexer.model;

import java.util.ArrayList;

public class Resource {
	private String filename;

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
	private ArrayList<Resource> resoucesList;

	public Resource(boolean isDir) {
		this.isDir = isDir;
		if (isDir)
			resoucesList = new ArrayList<Resource>();
	}

	public void addResource(Resource rsc) {
		if (this.isDir) {
			resoucesList.add(rsc);
		}
	}

	public Resource[] getResourceList() {
		return (Resource[]) this.resoucesList.toArray();
	}

	// print all the file names and directory names of this resource.
	// if the resource has folder type resources, call toString on them.
	public String toString() {
		String value = "";
		return value;
	}
}