package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MNT on 07-Apr-15.
 */
public class AllProcedureVm {

	public String id;
	public String doctorId;
	public String procedureName;
	public String numberOfTemplate;
	public String category;
        
	public List<AllTemplateVm> allTemplate = new ArrayList<AllTemplateVm>();
 
}
