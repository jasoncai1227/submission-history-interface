# submission-history

SubmissionHistory objects allow you to keep track of Submission objects. The SubmissionHistory interface has the following methods:

// Find the highest grade of any submission for a given student
public Integer getBestGrade(String unikey);

// The most recent submission for a given student
public Submission getSubmissionFinal(String unikey);

// The most recent submission for a given student, prior to a given time
public Submission getSubmissionBefore(String unikey, Date deadline);

// Add a new submission (can assume submissions from one student have different times)
public Submission add(String unikey, Date timestamp, Integer grade);

// Remove a submission (can assume submissions from one student have different times)
public void remove(Submission submission);

// Get all the students who have the highest grade
public List<String> listTopStudents();

// Get all the students whose most recent submission has lower grade than their best submission
public List<String> listRegressions();
  
For all of the interface methods, if any of the arguments passed to the method are null, then you should throw new IllegalArgumentException();

All methods run in sub-linear time (i.e. better than O(n), where n is the number of submissions. 

Exceptions:

listRegressions should be better than O(n^2) time
listTopStudents should be better than O(n) time, with the assumption that the number of students being returned is sub-linear.
Submission objects

Each Submission object has a unikey (String), a timestamp (java.util.Date), and a grade (Integer).
