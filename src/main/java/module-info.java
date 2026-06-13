module eus.ehu {
    requires openai.java;
    requires openai.java.core;
    requires openai.java.client.okhttp;
    requires com.google.gson;

    exports eus.ehu;
    opens eus.ehu to com.google.gson;
}