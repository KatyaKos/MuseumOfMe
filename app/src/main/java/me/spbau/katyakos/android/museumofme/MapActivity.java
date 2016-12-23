package me.spbau.katyakos.android.museumofme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.spbau.katyakos.android.museumofme.UserInformation.Trip;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

public class MapActivity extends Activity {

    @InjectView(R.id.map_list)
    ExpandableListView listLayout;
    @InjectView(R.id.map_menu_button)
    Button backButton;
    @InjectView(R.id.map_add_group_field)
    EditText addGroupField;
    @InjectView(R.id.map_add_group_button)
    Button addGroupButton;

    private TreeMap<Integer, Trip> mapList;
    private Integer userId;
    private UserInformation user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.inject(this);

        Intent thisIntent = getIntent();
        userId = thisIntent.getIntExtra("userId", 0);
        user = AllUsersInformation.getUserById(userId);
        mapList = user.getUserMap();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
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
                user.addGroup(getString(addGroupField));
                listLayout.setAdapter((BaseExpandableListAdapter) null);
                mapListCreate();
                addGroupField.setText("");
                getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        mapListCreate();
    }

    private <T extends TextView> String getString(T textView) {
        return textView.getText().toString();
    }

    private void mapListCreate() {
        ArrayList<Map<String, String>> groupNames;
        ArrayList<Map<String, String>> dataItem;
        ArrayList<ArrayList<Map<String, String>>> dataGroup;
        Map<String, String> attribute;

        groupNames = new ArrayList<>();
        dataGroup = new ArrayList<>();
        Set<Entry<Integer, Trip>> usersEntry = mapList.entrySet();
        for (Entry<Integer, Trip> entry : usersEntry) {
            Trip group = entry.getValue();
            attribute = new HashMap<>();
            attribute.put("groupId", group.getGroupId().toString());
            attribute.put("groupName", group.getGroupName());
            groupNames.add(attribute);

            dataItem = new ArrayList<>();
            TreeMap<Integer, String> places = group.getPlaces();
            Set<Entry<Integer, String>> placesEntry = places.entrySet();
            for (Entry<Integer, String> placeEntry : placesEntry) {
                attribute = new HashMap<>();
                attribute.put("placeId", placeEntry.getKey().toString());
                attribute.put("placeName", placeEntry.getValue());
                dataItem.add(attribute);
            }
            dataGroup.add(dataItem);
        }
        String groupFrom[] = new String[]{"groupId", "groupName"};
        int groupTo[] = new int[]{R.id.map_group_id, R.id.map_group_name};
        String childFrom[] = new String[]{"placeId", "placeName"};
        int childTo[] = new int[]{R.id.map_place_id, R.id.map_place_name};

        listLayout.setAdapter(adapterCreate(this, groupNames, R.layout.item_expandable_listview_group, groupFrom, groupTo,
                dataGroup, R.layout.item_expandable_listview_place, childFrom, childTo));
    }

    private Integer getIntegerFromParentView(ViewParent view, int id) {
        return Integer.getInteger(getString(((TextView) ((View) view).findViewById(id))));
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
                            Integer id = getIntegerFromParentView(item, R.id.map_group_id);
                            user.removeGroup(id);
                            mapList = user.getUserMap();
                            listLayout.setAdapter((BaseExpandableListAdapter) null);
                            mapListCreate();
                        }
                    });

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newPlace = getString(addPlaceField);
                            if (newPlace.isEmpty()) {
                                return;
                            }
                            Button clicked = (Button) view;
                            ViewParent item = clicked.getParent();
                            Integer id = getIntegerFromParentView(item, R.id.map_group_id);
                            user.addPlace(id, newPlace);
                            mapList = user.getUserMap();
                            listLayout.setAdapter((BaseExpandableListAdapter) null);
                            mapListCreate();
                            addPlaceField.setText("");
                            getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    });
                }

                String content = getGroup(groupPosition).toString();
                TextView nameText = (TextView) convertView.findViewById(R.id.map_group_name);
                TextView idText = (TextView) convertView.findViewById(R.id.map_group_id);
                int nameIndex = content.indexOf("groupName=");
                int idIndex = content.indexOf("groupId=");
                if (nameIndex < idIndex) {
                    nameText.setText(content.substring(nameIndex + 10, idIndex - 2));
                    idText.setText(content.substring(idIndex + 10, content.length() - 1));
                } else {
                    nameText.setText(content.substring(nameIndex + 10, content.length() - 1));
                    idText.setText(content.substring(idIndex + 10, nameIndex - 2));
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
                            Integer placeId = getIntegerFromParentView(item, R.id.map_place_id);
                            item = item.getParent();
                            Integer groupId = getIntegerFromParentView(item, R.id.map_group_id);
                            user.removePlace(groupId, placeId);
                            mapList = user.getUserMap();
                            listLayout.setAdapter((BaseExpandableListAdapter) null);
                            mapListCreate();
                        }
                    });
                }

                String content = getChild(groupPosition, childPosition).toString();
                TextView nameText = (TextView) convertView.findViewById(R.id.map_name);
                TextView dateText = (TextView) convertView.findViewById(R.id.map_date);
                int nameIndex = content.indexOf("placeName=");
                int dateIndex = content.indexOf("placeId=");
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
