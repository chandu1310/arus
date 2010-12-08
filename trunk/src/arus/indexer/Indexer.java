package arus.indexer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
	
	private ZipOutputStream createArchiveFile(String filename)
	{
		ZipOutputStream out = null;
		try
		{
			out = new ZipOutputStream(new 
		    BufferedOutputStream(new FileOutputStream(filename)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return out;
	}	
	
	private void writeFileToArchieve(ZipOutputStream zout, String entry, File f) throws Exception
	{
		byte[] data = new byte[1000];               
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		int count;
	
		zout.putNextEntry(new ZipEntry(entry));
		
		while((count = in.read(data,0,1000)) != -1)
		{      
			zout.write(data, 0, count);
		}
		in.close();
		zout.flush();
		System.out.println("File: "+entry+" is zipped");		
	}
	

	public void generatePatchDescriptor(String applicationName,
							String author,
							String key,
							File configfile)
	{				
		try {
			BufferedWriter writer = new BufferedWriter( 
										new OutputStreamWriter (
													new FileOutputStream(configfile) ) );
			if(rsc != null)
			{
				String str = "<arus application-name=\""+applicationName+"\" version=\""+versionString+"\" owner=\""+author+"\" key=\""+key+"\">\n";
				writer.write(str);
				rsc.writeXML(writer);
				writer.write("</arus>\n");
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generatePatch(	String applicationName,
								String author,
								String key,
								String patchLocation) throws Exception
	{
		String configfilename=patchLocation+"\\"+applicationName+"_arus-config.pen";		
		File configfile = new File(configfilename);
		if(configfile.exists())
		{
			// TODO: Resolve the version differences and update 
			//the conf file and archives.
			// - Regenerate the rsc variable.
		}
		else
		{
			// TODO: Index and generate the config file and create archive 
			//with initial version. reIndex is necessary to populate rsc variable.
			reIndex();
			generatePatchDescriptor(applicationName, author, key, configfile);
			File instFile = new File(patchLocation);
			if(!instFile.exists())
				instFile.mkdir();
			ZipOutputStream zout = createArchiveFile(patchLocation+"\\"+applicationName+"_aruspatch_"+versionString+".zip");
			writeResourceToArchieve(rsc, ".", zout, configfile);
			zout.close();
		}
	}
	
	private void writeResourceToArchieve(Resource r, String path, ZipOutputStream zout, File configFile) throws Exception
	{
		if(".".equals(path))			
		{
			writeFileToArchieve(zout, configFile.getName(), configFile);
			path = "";
			for(Resource t: r.getResourceList())
			{
				writeResourceToArchieve(t, path, zout, configFile);
			}
		}
		else
		{
			if(r.isDir())
			{
				path=path+"\\"+r.getFilename();
				for(Resource t: r.getResourceList())
				{
					writeResourceToArchieve(t, path, zout, configFile);
				}
			}
			else
			{
				String filePath=INST_TOP+path+"\\"+r.getFilename();
				File af = new File(filePath);
				writeFileToArchieve(zout, path+"\\"+r.getFilename(), af);
			}
		}
	}
	
	public static void main(String[] args) {		
		try {
			Indexer ind = new Indexer("D:\\Temp\\arus-test\\DemoApplication");
//			ind.reIndex();
			//ind.displayIndex(ind.getIndexedResource());
			ind.generatePatch("DemoApplication", "Chandu", "02241a1210", "D:\\Temp\\arus-test\\DemoApplication-patches");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
}