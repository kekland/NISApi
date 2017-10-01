# NISApi
API for NIS - to make easy android applications that helps NIS students

# Install

1. Add the following line to your app-level build.gradle<code>    compile 'com.loopj.android:android-async-http:1.4.9'</code>

2. Download this repository and put both folders into your *java* folder

3. Add INTERNET permission into your firebase.

You are all set now!

# Usage
All functions are in NISApi class.

## NISApi.Login
### Usage :

Used to log in into system. If everything was successful it saves user data into static NISData class and updates cookies.

**NOTE : **
If role of user is Parent, it also saves children list into NISData. If user has more than 1 child 
you should handle this and show dialog / spinner
with list of children to select, then use function NISData.setSelectedChild(NISChild child) to save it.

### Arguments : 

**PIN** - *String* - Personal Identification Number of user (ИИН)

**Pass** - *String* - Password of user

**School** - *String* - School, for example : <code> http://fmalm.nis.edu.kz/Almaty_Fmsh </code> 

**All school URLs must be in the same format!**

**listener** - *NISApiAccount.LoginListener* - Listener, has 3 functions : onStart(), onSuccess(), onFailure(String error)

### Example :
<code>

    void LogIntoSystem(String PIN, String Pass) {
        NISApi.Login(PIN, Pass, "http://fmalm.nis.edu.kz/Almaty_Fmsh", new NISApiAccount.LoginListener() {
            @Override
            public void onStart() {
                //TODO : Show dialog or start loading bar
            }
            @Override
            public void onSuccess() {
                //TODO : Everything was successful - show next activity with lessons or other things
            }
            @Override
            public void onFailure(String message) {
                //TODO : Display error message
            }
        });
    }
    
</code>

## NISApi.CheckCredentials
### Usage :

Used to check credentials of user - it is faster than logging in, but it does not update cookies.

### Arguments : 

**PIN** - *String* - Personal Identification Number of user (ИИН)

**Pass** - *String* - Password of user

**School** - *String* - School, for example : <code> http://fmalm.nis.edu.kz/Almaty_Fmsh </code> 

**All school URLs must be in the same format!**

**listener** - *NISApiAccount.LoginListener* - Listener, has 3 functions : onStart(), onSuccess(), onFailure(String error)

### Example :
<code>

    void CheckUserCredentials(String PIN, String Pass) {
        NISApi.CheckUserCredentials(PIN, Pass, "http://fmalm.nis.edu.kz/Almaty_Fmsh", new NISApiAccount.LoginListener() {
            @Override
            public void onStart() {
                //TODO : Show dialog or start loading bar
            }
            @Override
            public void onSuccess() {
                //TODO : Everything was successful - PIN and Pass is correct
            }
            @Override
            public void onFailure(String message) {
                //TODO : Display error message
            }
        });
    }
    
</code>

## NISApi.TryToLogin
### Usage :

Used primarily to register - first, it tries to log in with given password, 
if it fails, it tries to log in with default password ("Qqwerty1!"), and if it also fails,
fires onFailure();
If everything succeeds, it will save data into NISData class.

**NOTE : **
If role of user is Parent, it also saves children list into NISData. If user has more than 1 child 
you should handle this and show dialog / spinner
with list of children to select, then use function NISData.setSelectedChild(NISChild child) to save it.

### Arguments : 

**PIN** - *String* - Personal Identification Number of user (ИИН)

**Pass** - *String* - Password of user

**School** - *String* - School, for example : <code> http://fmalm.nis.edu.kz/Almaty_Fmsh </code> 

**All school URLs must be in the same format!**

**listener** - *NISApiAccount.LoginListener* - Listener, has 3 functions : onStart(), onSuccess(), onFailure(String error)

### Example :
<code>

    void Register(String PIN, String Pass) {
        NISApi.TryToLogin(PIN, Pass, "http://fmalm.nis.edu.kz/Almaty_Fmsh", new NISApiAccount.LoginListener() {
            @Override
            public void onStart() {
                //TODO : Show dialog or start loading bar
            }
            @Override
            public void onSuccess() {
                //TODO : Everything was successful - PIN and Pass is correct
            }
            @Override
            public void onFailure(String message) {
                //TODO : Display error message
            }
        });
    }
    
</code>

## NISApi.GetIMKOSubjects
### Usage :

Used to get IMKO subjects - call this function only when you have successfully logged in.
Uses data from NISData class.

### Arguments : 

**listener** - *NISApiSubjects.IMKOSubjectsListener* - Listener, has 3 functions : onStart(), onSuccess(SubjectData data), onFailure(String error)

### Example :
<code>

    void GetLessons(String PIN, String Pass) {
        NISApi.GetIMKOSubjects(new NISApiSubjects.IMKOSubjectsListener() {
            @Override
            public void onStart() {
                //TODO : Show that update is in progress
            }

            @Override
            public void onSuccess(SubjectData data) {
                //Get list of subjects in first period
                List<IMKOLesson> subjects = data.GetQuarter(0).GetSubjectsIMKO();
                //IMPORTANT : do not use GetSubjectsJKO() when you are getting IMKO lessons
                
                //Get name of first lesson - be aware that this list can be empty
                if(subjects.size() > 0) {
                    IMKOLesson first = subjects.get(0);
                    String name = first.Name; //Example : Английский язык
                    String toDisplayFormative = first.GetFormativeString(); //Example : 0 | 15
                    String toDisplaySummative = first.GetSummativeString(); //Example : 5 | 30
                    String grade = first.GetGrade(); //Example : 4
                }
            }

            @Override
            public void onFailure(String message) {
                //TODO : Show error message
            }
        });
    }
    
</code>

## NISApi.GetJKOSubjects
### Usage :

Used to get JKO subjects - call this function only when you have successfully logged in.
Uses data from NISData class.

### Arguments : 

**listener** - *NISApiSubjects.JKOSubjectsListener* - Listener, has 3 functions : onStart(), onSuccess(SubjectData data), onFailure(String error)

### Example :
<code>

    void GetLessons(String PIN, String Pass) {
        NISApi.GetJKOSubjects(new NISApiSubjects.JKOSubjectsListener() {
            @Override
            public void onStart() {
                //TODO : Show that update is in progress
            }

            @Override
            public void onSuccess(SubjectData data) {
                //Get list of subjects in first period
                List<JKOLesson> subjects = data.GetQuarter(0).GetSubjectsJKO();
                //IMPORTANT : do not use GetSubjectsIMKO() when you are getting JKO lessons

                //Get name of first lesson - be aware that this list can be empty
                if(subjects.size() > 0) {
                    JKOLesson first = subjects.get(0);
                    String name = first.Name; //Example : Английский язык
                    String toDisplayPercent = first.Percent; //Example : 89%
                    String grade = first.Mark; //Example : 4
                }
            }

            @Override
            public void onFailure(String message) {
                //TODO : Show error message
            }
        });
    }
    
</code>

## NISApi.GetIMKOGoals
### Usage :

Used to get IMKO goals, including list of homework.

### Arguments : 

**lesson** - *IMKOLesson* - Lesson to get goals from  

**listener** - *NISApiSubjects.IMKOMarksListener* - Listener, has 3 functions : onStart(), onSuccess(GoalsData data), onFailure(String error)

### Example :
<code>

    void GetIMKOGoals(IMKOLesson lesson) {
        NISApi.GetIMKOGoals(lesson, new NISApiSubjects.IMKOMarksListener() {
            @Override
            public void onStart() {
                //TODO : Show that update is in progress
            }

            @Override
            public void onSuccess(GoalsData data) {
                //Get data
                List<IMKOGoal> goals = data.IMKOGoals;
                List<Homework> homework = data.HomeworkList;
                //IMPORTANT : do not get data.JKOGoals when getting IMKO goals - it can cause NullPointerException

                //Warning : goals or homework list size can be zero if it is empty.
                if(goals.size() > 0) {
                    IMKOGoal first = goals.get(0);
                    
                    //Status of goal : Achieved, NotAssessed, WorkingTowards
                    GoalStatus status = first.Status;
                    
                    if(status == GoalStatus.WorkingTowards) {
                        //Comment of teacher
                        String teacherComment = first.Comment;
                    }
                    
                    //Name of goal
                    String name = first.Name;
                }
                
                if(homework.size() > 0) {
                    Homework first = homework.get(0);
                    
                    //List of downloadable URIs, open link to start downloading
                    List<String> urls = first.Files;
                    
                    //Description of homework, can be empty.
                    String desc = first.Description;
                }
            }

            @Override
            public void onFailure(String message) {
                //TODO : Display error message
            }
        });
    }
    
</code>

## NISApi.GetJKOGoals
### Usage :

Used to get JKO goals.

### Arguments : 

**lesson** - *JKOLesson* - Lesson to get goals from  

**listener** - *NISApiSubjects.JKOMarksListener* - Listener, has 3 functions : onStart(), onSuccess(GoalsData data), onFailure(String error)

### Example :
<code>

    void GetJKOGoals(JKOLesson lesson) {
        NISApi.GetJKOGoals(lesson, new NISApiSubjects.JKOMarksListener() {
            @Override
            public void onStart() {
                //TODO : Show that update is in progress
            }

            @Override
            public void onSuccess(GoalsData data) {
                //Get data
                List<JKOGoal> goals = data.JKOGoals;
                //IMPORTANT : do not get data.IMKOGoals when getting JKO goals - it can cause NullPointerException

                //Warning : goals list size can be zero if it is empty.
                if(goals.size() > 0) {
                    JKOGoal first = goals.get(0);

                    //Name of goal
                    String name = first.Name;
                    
                    //Points, for example : 3 | 10
                    String pointsForSection = first.sectionScore + " | " + first.sectionMaxScore;
                    String pointsForSummative = first.summativeScore + " | " + first.summativeMaxScore;
                    
                    //Overall percentage, i.e. 30
                    Integer percentage = first.GetPercentage();
                }
            }

            @Override
            public void onFailure(String message) {
                //TODO : Display error message
            }
        });
    }
    
</code>

## NISApi.ChangePassword
### Usage :

Used to change user password.
User should be authenticated before changing password.
Also, it uses data from NISData, therefore it can cause NullPointerException if user was not authenticated beforehand.
Your password also should be strong enough (you can check it with NISApi.GetPasswordStrength) to succeed.

### Arguments : 

**OldPassword** - *String* - Old password  

**NewPassword** - *String* - Password to change to

**listener** - *NISApiAccount.ChangePasswordListener* - Listener, has 3 functions : onStart(), onSuccess(), onFailure(String error)

### Example :
<code>

    void ChangePassword(String passwordBefore, String passwordAfter) {
        NISApi.ChangePassword(passwordBefore, passwordAfter, new NISApiAccount.ChangePasswordListener() {
            @Override
            public void onStart() {
                //TODO : Show that updating is in process
            }

            @Override
            public void onSuccess() {
                //TODO : Show message that password has changed
            }

            @Override
            public void onFailure(String message) {
                //TODO : Show error message
            }
        });
    }
    
</code>

## NISApi.GetPasswordStrength
### Usage :

Used to get password strength.
Does not require credentials.
If strength is below 3 - it is counted as weak password, so you cannot use it to change password or log in.

### Arguments : 

**password** - *String* - Password to get strength of

**listener** - *NISApiMisc.PassStrengthListener* - Listener, has 3 functions : onStart(), onSuccess(Integer strength), onFailure(String error)

### Example :
<code>

    void CheckStrength(String password) {

        NISApi.GetPasswordStrength(password, new NISApiMisc.PassStrengthListener() {
            @Override
            public void onStart() {
                //TODO : Show that updating is in process
            }

            @Override
            public void onSuccess(int strength) {
                //TODO : Use strength to change password or other things
                if(strength < 3) {
                    //Too weak, for example : password
                }
                else {
                    //Strong, for example : Qqwerty1!
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
    
</code>

## Other stuff to check :

## NISApiUtils contains useful elements :

### NISApiUtils.URLs 
    
Contains URLs of all schools.
    
<code>
        
    public static ArrayList<String> URLs = new ArrayList<String>()
    {{
        add("http://akt.nis.edu.kz/Aktau");
        add("http://akb.nis.edu.kz/Aktobe");
        add("http://fmalm.nis.edu.kz/Almaty_Fmsh");
        add("http://hbalm.nis.edu.kz/Almaty_HBSH");
        add("http://ast.nis.edu.kz/Astana_FMSH");
        add("http://atr.nis.edu.kz/Atyrau");
        add("http://krg.nis.edu.kz/Karaganda");
        add("http://kt.nis.edu.kz/Kokshetau");
        add("http://kst.nis.edu.kz/Kostanay");
        add("http://kzl.nis.edu.kz/Kyzylorda");
        add("http://pvl.nis.edu.kz/Pavlodar");
        add("http://ptr.nis.edu.kz/Petropavlovsk");
        add("http://sm.nis.edu.kz/Semey_FMSH");
        add("http://tk.nis.edu.kz/Taldykorgan");
        add("http://trz.nis.edu.kz/Taraz");
        add("http://ura.nis.edu.kz/Uralsk");
        add("http://ukk.nis.edu.kz/Oskemen");
        add("http://fmsh.nis.edu.kz/Shymkent_FMSH");
        add("http://hbsh.nis.edu.kz/Shymkent_HBSH");
    }};
    
</code>

### NISApiUtils.SchoolNames 
    
Contains names of all schools - in the same order as URLs.
    
<code>
    
    public static ArrayList<String> SchoolNames = new ArrayList<String>()
    {{
        add("Aktau CBD");
        add("Aktobe PhMD");
        add("Almaty PhMD");
        add("Almaty CBD");
        add("Astana PhMD");
        add("Atyrau CBD");
        add("Karaganda CBD");
        add("Kokshetau PhMD");
        add("Kostanay PhMD");
        add("Kyzylorda CBD");
        add("Pavlodar CBD");
        add("Petropavlovsk CBD");
        add("Semey PhMD");
        add("Taldykorgan PhMD");
        add("Taraz PhMD");
        add("Uralsk PhMD");
        add("Ust-Kamenogorsk CBD");
        add("Shymkent PhMD");
        add("Shymkent CBD");
    }};

</code>

### GetSchoolNameByLink(String link)

Used to get school name by link.
