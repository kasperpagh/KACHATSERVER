/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatka;

import java.util.ArrayList;

/**
 *
 * @author pagh
 */
public class UserList
{

    private ArrayList userList;

    public UserList()
    {
        userList = new ArrayList();
    }
    
    public void setUserList(ArrayList a)
    {
        userList = a;
    }

    public ArrayList getUserList()
    {
        return userList;
    }
    
    public void addToList(String a)
    {
        userList.add(a);
    }
}
