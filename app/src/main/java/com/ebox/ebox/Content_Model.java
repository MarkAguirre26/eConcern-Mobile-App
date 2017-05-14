package com.ebox.ebox;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mark on 11/15/2016.
 */

public class Content_Model {

    @SerializedName("cn")
    public String cn;
    @SerializedName("Sender_name")
    public String Sender_name;
    @SerializedName("subject_name")
    public String subject_name;
    @SerializedName("Message")
    public String Message;
    @SerializedName("File_path")
    public String File_path;
    @SerializedName("Date_created")
    public String Date_created;

}
