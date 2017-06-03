package com.pairtodobeta.ui.main.chat.fixtures;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by Anton Bevza on 1/13/17.
 */

public class FixturesData {

    protected static ArrayList<String> avatars = new ArrayList<String>() {
        {
            add("http://v2.api.pairtodoalpha.com/uploads/e8330a547d6dced5f650b5a07ac99c8f81c16e54.jpg");
            add("http://v2.api.pairtodoalpha.com/uploads/e8330a547d6dced5f650b5a07ac99c8f81c16e54.jpg");
            add("http://i.imgur.com/ROz4Jgh.png");
            add("http://v2.api.pairtodoalpha.com/uploads/e8330a547d6dced5f650b5a07ac99c8f81c16e54.jpg");
        }
    };
    protected static final ArrayList<String> groupChatImages = new ArrayList<String>() {
        {
            add("http://i.imgur.com/hRShCT3.png");
            add("http://i.imgur.com/zgTUcL3.png");
            add("http://i.imgur.com/mRqh5w1.png");
        }
    };
    protected static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("Samuel Reynolds");
            add("Kyle Hardman");
            add("Zoe Milton");
            add("Angel Ogden");
            add("Zoe Milton");
            add("Angelina Mackenzie");
            add("Kyle Oswald");
            add("Abigail Stevenson");
            add("Julia Goldman");
            add("Jordan Gill");
            add("Michelle Macey");
        }
    };
    protected static final ArrayList<String> groupChatTitles = new ArrayList<String>() {
        {
            add("Samuel, Michelle");
            add("Jordan, Jordan, Zoe");
            add("Julia, Angel, Kyle, Jordan");
        }
    };
    protected static final ArrayList<String> messages = new ArrayList<String>() {
        {
            add("Ты где?!?!? Молоко купить не забыл??!");
            add("Я уже в пути на крыльях любви");
            add("Жду тебя)");
            add("Да, привет! Как я мог забыть;)");
            add("Привет, дорогой. ТЫ сделал мое поручение?? Я тебе задачу ставила)");
        }
    };
    public static SecureRandom rnd = new SecureRandom();

}
