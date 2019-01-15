package com.example.android.homework4_1;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ListView list_of_tasks;
    //ArrayList countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    private ArrayList<String> tasks;
    private ArrayAdapter<String> arrayAdapter ;
    private static final String FILE_NAME = "New_To_do_list.txt";
            //= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);


    private TextToSpeech textToSpeech;
    private boolean speechReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_of_tasks = (ListView) findViewById(R.id.listOfTasks);
        tasks = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);

        list_of_tasks.setAdapter(arrayAdapter);
        list_of_tasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tasks.remove(position);
                try{
                    PrintWriter writer = new PrintWriter(FILE_NAME);
                    writer.print("");
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Copy the new list into the file
                try {
                    PrintStream printStream = new PrintStream(openFileOutput(FILE_NAME, MODE_PRIVATE));
                    for (int i = 0; i < tasks.size(); i++) {
                    printStream.println(tasks.get(i));
                    }
                    printStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

        list_of_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (speechReady) {
                    textToSpeech.speak(tasks.get(position), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speechReady = true;
            }
        });
        //list_of_tasks = (ListView) findViewById(R.id.listOfTasks);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        //list_of_tasks.setAdapter(arrayAdapter);
    }

    public void addNewTask(View view) {
        EditText n = (EditText) findViewById(R.id.newTask);
        String newTask = n.getText().toString();
        tasks.add(newTask);
        try {
            PrintStream printStream = new PrintStream(openFileOutput(FILE_NAME, MODE_PRIVATE));
            for (int i = 0; i < tasks.size(); i++) {
                printStream.println(tasks.get(i));
            }
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //list_of_tasks = (ListView) findViewById(R.id.listOfTasks);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        //list_of_tasks.setAdapter(arrayAdapter);
        n.setText("");
        arrayAdapter.notifyDataSetChanged();


    }
@Override
    protected void onResume() {
        super.onResume();

        try {
            Scanner scanner = new Scanner(openFileInput(FILE_NAME));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                tasks.add(line);
            }
            scanner.close();
            arrayAdapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    /**
    @Override

    protected void onPause() {
        super.onPause();

        try {
            PrintStream printStream = new PrintStream(openFileOutput(FILE_NAME, MODE_PRIVATE));
            for (int i = 0; i < tasks.size(); i++) {
                printStream.println(tasks.get(i));
            }
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
     **/


}
