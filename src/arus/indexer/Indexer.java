package arus.indexer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import arus.indexer.model.Resource;

public class Indexer {
	private String INST_TOP;

	private Resource rsc;

	private boolean isIndexed = false;
	
	private String versionString = "1.0";

	public Indexer(String INST_TOP) throws Exception
	{
		this.INST_TOP = INST_TOP;
		reIndex();
	}

	public boolean indexed()
	{
		return this.isIndexed;
	}

	public Resource getIndexedResource()
	{
		if (isIndexed)
			return this.rsc;
		else
			return null;
	}

	public void reIndex() throws Exception
	{
		if(rsc == null || !isIndexed )    
		{
			File parentDir = new File(INST_TOP);
			rsc = new Resource(true);
				populateResourceObject(rsc, parentDir);			
			indexIt(parentDir, rsc);
			
		}
		isIndexed = true;
    }
	
	private void indexIt(File parentDir, Resource r_out)
	{
		File[] contents = parentDir.listFiles();
		for(File f: contents)
		{	
			Resource r_in = new Resource(f.isDirectory());
				populateResourceObject(r_in, f);
			r_out.addResource(r_in);
			if(f.isDirectory())
			{
				indexIt(f, r_in);
			}			
		}
	}
	
	private void populateResourceObject(Resource r, File f)
	{
//		String absolutePath = f.getAbsolutePath();
//			absolutePath = absolutePath.substring(INST_TOP.length());
//			absolutePath = absolutePath.trim();
//			if("".equals(absolutePath))
//				absolutePath = "\\";
//		r.setFilename(absolutePath);
		r.setFilename(f.getName());
		r.setVersion(versionString);
	}
	
	public void displayIndex(Resource r)
	{
		System.out.println(r.getFilename());
		for(Resource t: r.getResourceList())
		{
			if(t.isDir())
				displayIndex(t);
			else
				System.out.println(t.getFilename());
		}
	}

	public void generateXML(String applicationName,
							String author,
							String key,
							String filename)
	{				
		try {
			BufferedWriter writer = new BufferedWriter( 
										new OutputStreamWriter (
													new FileOutputStream(filename) ) );
			if(rsc != null)
			{
				String str = "<arus application-name=\""+applicationName+"\" version=\""+versionString+"\" owner=\""+author+"\" key=\""+key+"\">\n";
				writer.write(str);
				rsc.writeXML(writer);
				writer.write("</arus>\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public static void main(String[] args) {
		
		try {
			Indexer ind = new Indexer("D:\\Uday Projects\\ARUS\\Source\\Eclipse Workspace\\MyTestSoftware");
			ind.reIndex();
//			ind.displayIndex(ind.getIndexedResource());
			ind.generateXML("WinHTT", "Uday", "02241a1210", "output.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
}