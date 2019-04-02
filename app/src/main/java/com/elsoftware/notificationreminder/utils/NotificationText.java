package com.elsoftware.notificationreminder.utils;

/**
 * Created by popovich on 01.10.2016.
 */

public class NotificationText
{
    protected String mTitle;
    protected String mDescription;
    protected int mTitleLength = 20;

    public NotificationText(String text)
    {
        mTitle = "";
        mDescription = "";

        if(text.length() < mTitleLength)
        {
            mTitle = text;
            return;
        }

        int lineCharacter = text.indexOf("\n");
        if(lineCharacter != -1)
        {
            mTitle = text.substring(0, lineCharacter);
            mDescription = text.substring(lineCharacter);
            return;
        }

        String[] words = text.split(" ");
        int currentWord = 0;

        if(words.length > 0) {
            mTitle = words[0];
            currentWord++;

            while (mTitle.length() < mTitleLength && currentWord < words.length) {
                mTitle += " " + words[currentWord];
                currentWord++;
            }

            int i = currentWord;
            while (currentWord < words.length) {
                if (currentWord != i) mDescription += " ";
                mDescription += words[currentWord];
                currentWord++;
            }
        }
    }

    public Boolean isDescriptionExist() { return mDescription != null && mDescription.length() > 0; }

    public String getTitle() { return mTitle; }

    public String getDescription() { return mDescription; }

}
