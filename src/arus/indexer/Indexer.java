package arus.indexer;

import java.io.File;

import arus.indexer.model.Resource;

public class Indexer {
	private String INST_TOP;

	private Resource rsc;

	private boolean isIndexed = false;

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
			// TODO : By uday
			//  Indexing logic to be written
			//Clue: Understand java.io.File class

			File f = new File("c:/dummy.txt");
			if( ! f.exists() )
			    f.createNewFile();
		}
		isIndexed = true;
    }
}