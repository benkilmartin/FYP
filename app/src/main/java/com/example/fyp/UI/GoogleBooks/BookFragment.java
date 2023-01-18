package com.example.fyp.UI.GoogleBooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fyp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



public class BookFragment extends Fragment {


    private EditText mBookSearch;
    private Button sButton;
    private RequestQueue mRequestQueue;
    private ArrayList<Book> bookArrayList;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book,container,false);
        mBookSearch = view.findViewById(R.id.searchETa);
        sButton = view.findViewById(R.id.sbtn);
        mRecyclerView = view.findViewById(R.id.idRVBooks);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBookSearch.getText().toString().isEmpty()) {
                    mBookSearch.setError("Please enter search query");
                    return;
                }
                String query = mBookSearch.getText().toString();
                bookArrayList = new ArrayList<>();
                mRequestQueue = Volley.newRequestQueue(getActivity());
                mRequestQueue.getCache().clear();
                String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                System.out.println("Here");
                JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Hereer");
                        try {
                            JSONArray itemsArray = response.getJSONArray("items");
                            System.out.println("Here1");
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject itemsObj = itemsArray.getJSONObject(i);
                                JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                                String title = volumeObj.optString("title");
                                String subtitle = volumeObj.optString("subtitle");
                                JSONArray authorsArray = volumeObj.getJSONArray("authors");
                                String publisher = volumeObj.optString("publisher");
                                String publishedDate = volumeObj.optString("publishedDate");
                                String description = volumeObj.optString("description");
                                int pageCount = volumeObj.optInt("pageCount");
                                JSONObject thumbnailUrlObject = volumeObj.optJSONObject("imageLinks");
                                String thumbnail = thumbnailUrlObject.optString("thumbnail");
                                thumbnail = thumbnail.substring(0, 4) + 's' + thumbnail.substring(4);
                                String previewLink = volumeObj.optString("previewLink");
                                String infoLink = volumeObj.optString("infoLink");
                                JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                                String buyLink = saleInfoObj.optString("buyLink");
                                ArrayList<String> authorsArrayList = new ArrayList<>();
                                if (authorsArray.length() != 0) {
                                    for (int j = 0; j < authorsArray.length(); j++) {
                                        authorsArrayList.add(authorsArray.optString(i));
                                    }
                                }
                                Book book = new Book(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                                bookArrayList.add(book);
                                BookAdapter adapter = new BookAdapter(bookArrayList, getActivity());
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                mRecyclerView.setLayoutManager(linearLayoutManager);
                                mRecyclerView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "No Data Found" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error found is " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(booksObjrequest);
            }
        });
        return view;
    }
}