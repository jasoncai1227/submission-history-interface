import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Assignment implements SubmissionHistory {
	
	/**
	 * Default constructor
	 */
	//Tree Map used to store Unikey as a key, and Sub Tree Map as value, which store Date as a key and Submission as value
	private TreeMap<String,TreeMap<Date,Submission>> dateMap; 
	//Tree Map used to store Unikey as a key, and Sub Tree Map as value,which Tree map used to store Grade as a key, and Submission as value
	private TreeMap<String,TreeMap<Integer,Submission>>gradeMap;
	//Tree map storing the unikey as a key, then the value is a tree map with the grade as a key and the number of occurences of the grade by the unikey as the value. 
	private TreeMap<String,TreeMap<Integer,Integer>>CountingMap;
	//Tree Map used to store Grade as a key, and Sub Tree Map as value, which used to store Unikey as key and Submission as value
    private TreeMap<Integer,TreeMap<String,Submission>>Map;
   
	public Assignment(){
		dateMap=new TreeMap<>();
		gradeMap=new TreeMap<>();
		Map=new TreeMap<>();
	    CountingMap=new TreeMap<>();
		}
	//Implement the interface Submission
	public class Sub implements Submission{
		String unikey;
		Date time;
		Integer grade;
		public Sub(String unikey, Date timestamp, Integer grade){
			this.unikey=unikey;
			this.time=timestamp;
			this.grade=grade;
		}
		public String getUnikey(){
			return this.unikey;
		}
		public Date getTime(){
			return this.time;
		}
		public Integer getGrade(){
			return this.grade;
		}
		
	}
	
	@Override
	public Integer getBestGrade(String unikey) {
		if(unikey==null){
			throw new IllegalArgumentException("unikey is null");
		}
		//if the grade Tree Map does not contain the require unikey, then return null
		if(gradeMap.containsKey(unikey)==false){
			return null;
		}
		//find the corresponding sub tree to the require unikey, and return the the last key in the subtree.
		Integer result=gradeMap.get(unikey).lastKey();
	    // TODO Implement this, ideally in better than O(n)
		return result;
	}

	@Override
	public Submission getSubmissionFinal(String unikey) {
		if(unikey==null){
			throw new IllegalArgumentException();
		}
		if(dateMap.containsKey(unikey)==false){
			return null;
		}
		//find the corresponding sub tree to the require unikey, and return the the value in the last entry in the subtree.
		return dateMap.get(unikey).lastEntry().getValue();
	}

	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) {
		if(unikey==null||deadline==null){
			throw new IllegalArgumentException();
		}
	//find the entry whose key is greatest and less than or equal to the given deadline, if not find return null
	  if(dateMap.get(unikey).floorEntry(deadline)==null){
		  return null;
	  }
	 // if found, return the entry's value
	  Submission result=dateMap.get(unikey).floorEntry(deadline).getValue();
		// TODO Implement this, ideally in better than O(n)
		return result;
	}

	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) {
		if(unikey==null||timestamp==null||grade==null){
			throw new IllegalArgumentException();
		}
		Sub submission=new Sub(unikey,timestamp,grade);
		//if the dateMap does not contain the given key, add a new key in the dateMap and add a corresponding sub tree to the given unikey
		if(dateMap.containsKey(unikey)==false){
			TreeMap<Date,Submission>datesubMap1=new TreeMap<>();
			datesubMap1.put(timestamp, submission);
			dateMap.put(unikey, datesubMap1);
			}
		else{
		//if did contain, update the subtree and date tree
			dateMap.get(unikey).put(timestamp, submission);
		    dateMap.put(unikey, dateMap.get(unikey));
		}
		
			if(gradeMap.containsKey(unikey)==false){
	         	TreeMap<Integer,Submission>gradesubMap1=new TreeMap<>();
	         	gradesubMap1.put(grade,submission);
				gradeMap.put(unikey, gradesubMap1);
			}
	   else{
		gradeMap.get(unikey).put(grade,submission);
		gradeMap.put(unikey, gradeMap.get(unikey));
	   }
			
		    if(Map.containsKey(grade)==false){
		    	TreeMap<String,Submission>Map1=new TreeMap<>();
		    	Map1.put(unikey, submission);
		    	Map.put(grade, Map1);
		    }
		    else{
		    	Map.get(grade).put(unikey, submission);
		        Map.put(grade,Map.get(grade));
		    }
		 /*check the CountingMap contains unikey or not
		  * if not, add grade and count num 1 into subtree 
		  * add new sub tree to the main tree
		  */
		 if(CountingMap.containsKey(unikey)==false){
		    	TreeMap<Integer,Integer>CountingMap2=new TreeMap<>();
		    	CountingMap2.put(grade, new Integer(1));
		    	CountingMap.put(unikey, CountingMap2);
		    }
		    else{
		    	/*if subtree have already contain the grade,
		    	 * add 1 to corresponding counting number
		    	 * update the main tree
		    	 */
		     if(CountingMap.get(unikey).containsKey(grade)==true){
		    	 Integer increment=new Integer(1);
					Integer count;
					count=CountingMap.get(unikey).get(grade)+increment;
					CountingMap.get(unikey).put(grade,count);
					CountingMap.put(unikey, CountingMap.get(unikey));
					}
		     else{
		    	 /*if subtree does not contain the grade,
		    	  * add a new entry with grade and counting number 1 into the subtree,
		    	  * update the maintree with the new subtree
		    	  */
		         CountingMap.get(unikey).put(grade, new Integer(1));
		    	 CountingMap.put(unikey,CountingMap.get(unikey) );
		     }
		    }		 
		       
		// TODO Implement this, ideally in better than O(n)
		return submission;
	}

	@Override
	public void remove(Submission submission) {
		if(submission==null){
			throw new IllegalArgumentException();
		}
		// remove the corresponding date key and value in date map
		dateMap.get(submission.getUnikey()).remove(submission.getTime());
		/*if counting number corresponding to grade and unikey is 1,
		 * remove the submission
		 */
		Integer numCompare=new Integer(1);
		int result=0;
		result=CountingMap.get(submission.getUnikey()).get(submission.getGrade()).compareTo(numCompare);
		if(result==0){
			gradeMap.get(submission.getUnikey()).remove(submission.getGrade());
		}
		else{
			/*if counting number corresponding to given unikey and given grade is bigger than 1,
			 * only reduce the counting number by one
			 */
			Integer num=CountingMap.get(submission.getUnikey()).get(submission.getGrade())-numCompare;
			CountingMap.get(submission.getUnikey()).put(submission.getGrade(), num);
		}
		    
		// TODO Implement this, ideally in better than O(n)
		
	}

	@Override
	public List<String> listTopStudents() {
      
		List<String> listtop=new ArrayList<String>();
		if(Map.isEmpty()){
			return listtop;
		}
		Set<String> list =Map.lastEntry().getValue().keySet();
		listtop.addAll(list);
		// TODO Implement this, ideally in better than O(n)
		// (you may ignore the length of the list in the analysis)
		return listtop;
	}

	@Override
	public List<String> listRegressions() {
		//list that store the result
		List<String> listR=new ArrayList<>();
		//use keySet to get all the keys of the gradeMap into a set
		Set<String> list=gradeMap.keySet();// TODO Implement this, ideally in better than O(n^2)
		List<String> list2=new ArrayList<>();
		//put all elements in the set to an arrayList
		list2.addAll(list);
		//if last submission grade is less than best grade of the students, add into the R array list
		for(int n=0;n<list2.size();n++){
			if(this.getBestGrade(list2.get(n))>this.getSubmissionFinal(list2.get(n)).getGrade()){
				listR.add(list2.get(n));
		   }
			}
		return listR;
	}
}
