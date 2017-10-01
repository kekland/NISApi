package com.kekland.enis.NIS;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class NISData {

    private static String PIN;
    private static String Password;
    private static String School;
    private static String Nickname;
    private static NISRole Role;

    private static NISChild[] Children;
    private static NISChild SelectedChild;

    public static void Load(Context context) throws NullPointerException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        setPIN(prefs.getString("PIN", ""));
        setPassword(prefs.getString("Password", ""));
        setSchool(prefs.getString("School", ""));
        setNickname(prefs.getString("Nickname", ""));

        try {
            setRole(NISRole.valueOf(prefs.getString("Role", "")));
        }
        catch(Exception e) {
            throw new NullPointerException("Role is incorrect");
        }

        try {
            if(getRole() == NISRole.Parent) {
                int ChildrenCount = prefs.getInt("Children.Size", 0);
                int SelectedID = prefs.getInt("Children.Selected", 0);

                NISChild[] children = new NISChild[ChildrenCount];
                for(int i = 0; i < ChildrenCount; i++) {
                    String childName = prefs.getString("Children." + i + ".Child.Name", "");
                    String childID = prefs.getString("Children." + i + ".Child.ID", "");
                    String className = prefs.getString("Children." + i + ".Class.Name", "");
                    String classID = prefs.getString("Children." + i + ".Class.ID", "");
                    NISChild child = new NISChild(childName, childID, className, classID);
                    children[i] = child;
                    if(i == SelectedID) {
                        setSelectedChild(child);
                    }
                }
                setChildren(children);

            }
        }
        catch(Exception e) {
            throw new NullPointerException("Children are incorrect");
        }

    }

    public static void Save(Context context) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefs.putString("PIN", getPIN());
        prefs.putString("Password", getPassword());
        prefs.putString("School", getSchool());
        prefs.putString("Nickname", getNickname());

        prefs.putString("Role", getRole().toString());

        if(getRole() == NISRole.Parent) {
            NISChild[] children = getChildren();
            NISChild selectedChild = getSelectedChild();

            prefs.putInt("Children.Size", children.length);
            for (int i = 0; i < children.length; i++) {
                prefs.putString("Children." + i + ".Child.Name", children[i].studentName);
                prefs.putString("Children." + i + ".Child.ID", children[i].studentID);
                prefs.putString("Children." + i + ".Class.Name", children[i].className);
                prefs.putString("Children." + i + ".Class.ID", children[i].classID);

                if (selectedChild != null) {
                    if (children[i] == selectedChild) {
                        prefs.putInt("Children.Selected", i);
                    }
                }
            }
            setChildren(children);

        }

        prefs.apply();
    }

    public static void SaveInDatabase(FirebaseDatabase database, OnCompleteListener<Void> listener) {
        Map<String, String> DatabaseValues = new HashMap<>();

        DatabaseValues.put("PIN", getPIN());
        DatabaseValues.put("Password", getPassword());
        DatabaseValues.put("School", getSchool());
        DatabaseValues.put("Nickname", getNickname());
        DatabaseValues.put("Role", getRole().toString());

        database.getReference().child("users-v2")
                .child(NISApiUtils.ConvertURLToDatabaseURI(getSchool()))
                .child(PIN).setValue(DatabaseValues)
                .addOnCompleteListener(listener);
    }

    public static void GetFromDatabase(FirebaseDatabase database, String PIN, String School,
                                       final DatabaseListener listener) {
        listener.onStart();
        database.getReference()
                .child(NISApiUtils.ConvertURLToDatabaseURI(getSchool()))
                .child(PIN)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        setPIN(dataSnapshot.child("PIN").getValue().toString());
                        setPassword(dataSnapshot.child("Password").getValue().toString());
                        setSchool(dataSnapshot.child("School").getValue().toString());
                        setNickname(dataSnapshot.child("Nickname").getValue().toString());
                        setRole(NISRole.valueOf(dataSnapshot.child("Role").getValue().toString()));

                        listener.onSuccess();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onFailure(databaseError);
                    }
                });
    }

    public interface DatabaseListener {
        void onStart();
        void onSuccess();
        void onFailure(DatabaseError error);
    }



    //Getters and setters



    public static String getPIN() {
        return PIN;
    }

    public static void setPIN(String PIN) {
        NISData.PIN = PIN;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }

    public static String getSchool() {
        return School;
    }

    public static void setSchool(String school) {
        School = school;
    }

    public static String getNickname() {
        return Nickname;
    }

    public static void setNickname(String nickname) {
        Nickname = nickname;
    }

    public static NISRole getRole() {
        return Role;
    }

    public static void setRole(NISRole role) {
        Role = role;
    }

    public static NISChild[] getChildren() {
        return Children;
    }

    public static void setChildren(NISChild[] children) {
        Children = children;
    }

    public static NISChild getSelectedChild() {
        return SelectedChild;
    }

    public static void setSelectedChild(NISChild selectedChild) {
        SelectedChild = selectedChild;
    }
}
