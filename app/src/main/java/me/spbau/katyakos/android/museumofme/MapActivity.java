package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KatyaKos on 21.11.2016.
 */

public class MapActivity extends Activity {

    private ExpandableListView mapList;
    private Button menuButton;
    private EditText addGroupField;
    private Button addGroupButton;
    private ArrayList<String> userMap;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        menuButton = (Button) findViewById(R.id.map_menu_button);
        mapList = (ExpandableListView) findViewById(R.id.map_list);
        addGroupField = (EditText) findViewById(R.id.map_add_group_field);
        addGroupButton = (Button) findViewById(R.id.map_add_group_button);
        userMap = UserInformation.getUserMap();

        mapListCreate();
        buttonsOnClick();
    }

    private void mapListCreate() {
        ArrayList<Map<String, String>> groupNames;
        ArrayList<Map<String, String>> dataItem;
        ArrayList<ArrayList<Map<String, String>>> dataGroup;
        Map<String, String> attribute;

        groupNames = new ArrayList<>();
        for (String trip : userMap) {
            String[] pieces = trip.split("::");
            attribute = new HashMap<>();
            attribute.put("groupDate", pieces[0]);
            attribute.put("groupName", pieces[1]);
            groupNames.add(attribute);
        }
        String groupFrom[] = new String[]{"groupDate", "groupName"};
        int groupTo[] = new int[]{R.id.map_date, R.id.map_name};

        dataGroup = new ArrayList<>();
        for (String trip : userMap) {
            String[] pieces = trip.split("::");
            dataItem = new ArrayList<>();
            for (int i = 2; i < pieces.length; i++) {
                attribute = new HashMap<>();
                attribute.put("placeDate", pieces[0]);
                attribute.put("placeName", pieces[i]);
                dataItem.add(attribute);
            }
            dataGroup.add(dataItem);
        }
        String childFrom[] = new String[]{"placeDate", "placeName"};
        int childTo[] = new int[]{R.id.map_date, R.id.map_name};

        mapList.setAdapter(adapterCreate(this, groupNames, R.layout.item_expandable_listview_group, groupFrom, groupTo,
                dataGroup, R.layout.item_expandable_listview_place, childFrom, childTo));
    }

    private void buttonsOnClick() {
        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go to MainActivity
                finish();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);*/
            }
        });

        addGroupField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (addGroupField.getText().toString().isEmpty()) {
                    addGroupButton.setVisibility(View.GONE);
                } else {
                    addGroupButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        addGroupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());
                UserInformation.addGroup(date + "::" + addGroupField.getText().toString());
                mapList.setAdapter((BaseExpandableListAdapter) null);
                mapListCreate();
                addGroupField.setText("");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
    }

    private SimpleExpandableListAdapter adapterCreate(Context context, List<? extends Map<String, ?>> groupData,
                                                      int groupLayout, String[] groupFrom, int[] groupTo,
                                                      List<? extends List<? extends Map<String, ?>>> childData,
                                                      int childLayout, String[] childFrom, int[] childTo) {
        return new SimpleExpandableListAdapter(context, groupData, groupLayout, groupFrom, groupTo,
                childData, childLayout, childFrom, childTo) {
            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.item_expandable_listview_group, null);
                    Button deleteButton = (Button) convertView.findViewById(R.id.map_delete_group);
                    Button addButton = (Button) convertView.findViewById(R.id.map_add_place);
                    final EditText addPlaceField = (EditText) findViewById(R.id.map_add_place_field);

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Button clicked = (Button) view;
                            ViewParent item = clicked.getParent();
                            String date = ((TextView) ((View) item).findViewById(R.id.map_date)).getText().toString();
                            UserInformation.removeGroup(date);
                            userMap = UserInformation.getUserMap();
                            mapList.setAdapter((BaseExpandableListAdapter) null);
                            mapListCreate();
                        }
                    });

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newPlace = addPlaceField.getText().toString();
                            if (newPlace.isEmpty()) {
                                return;
                            }
                            Button clicked = (Button) view;
                            ViewParent item = clicked.getParent();
                            String date = ((TextView) ((View) item).findViewById(R.id.map_date)).getText().toString();
                            UserInformation.addPlace(date, newPlace);
                            userMap = UserInformation.getUserMap();
                            mapList.setAdapter((BaseExpandableListAdapter) null);
                            mapListCreate();
                            addPlaceField.setText("");
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    });
                }

                String content = getGroup(groupPosition).toString();
                TextView nameText = (TextView) convertView.findViewById(R.id.map_name);
                TextView dateText = (TextView) convertView.findViewById(R.id.map_date);
                int nameIndex = content.indexOf("groupName=");
                int dateIndex = content.indexOf("groupDate=");
                if (nameIndex < dateIndex) {
                    nameText.setText(content.substring(nameIndex + 10, dateIndex - 2));
                    dateText.setText(content.substring(dateIndex + 10, content.length() - 1));
                } else {
                    nameText.setText(content.substring(nameIndex + 10, content.length() - 1));
                    dateText.setText(content.substring(dateIndex + 10, nameIndex - 2));
                }

                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.item_expandable_listview_place, null);
                    Button deleteButton = (Button) convertView.findViewById(R.id.map_delete_place);

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Button clicked = (Button) view;
                            ViewParent item = clicked.getParent();
                            String place = ((TextView) ((View) item).findViewById(R.id.map_name)).getText().toString();
                            String date = ((TextView) ((View) item).findViewById(R.id.map_date)).getText().toString();
                            UserInformation.removePlace(date, place);
                            userMap = UserInformation.getUserMap();
                            mapList.setAdapter((BaseExpandableListAdapter) null);
                            mapListCreate();
                        }
                    });
                }

                String content = getChild(groupPosition, childPosition).toString();
                TextView nameText = (TextView) convertView.findViewById(R.id.map_name);
                TextView dateText = (TextView) convertView.findViewById(R.id.map_date);
                int nameIndex = content.indexOf("placeName=");
                int dateIndex = content.indexOf("placeDate=");
                if (nameIndex < dateIndex) {
                    nameText.setText(content.substring(nameIndex + 10, dateIndex - 2));
                    dateText.setText(content.substring(dateIndex + 10, content.length() - 1));
                } else {
                    nameText.setText(content.substring(nameIndex + 10, content.length() - 1));
                    dateText.setText(content.substring(dateIndex + 10, nameIndex - 2));
                }

                return convertView;
            }
        };

    }
}
