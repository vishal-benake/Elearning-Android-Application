package com.git.extc.activities.Semesters.Backend.Semester2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.git.extc.R;
import com.git.extc.activities.PDFActivity;
import com.git.extc.databinding.SemTwoActivitySubject3Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Subject3 extends AppCompatActivity {

    SemTwoActivitySubject3Binding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SemTwoActivitySubject3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        //final GridView myGridView = findViewById(R.id.gridview);
        // ImageView btnRetrive = findViewById(R.id.refreshBtn);
        // final ProgressBar myProgressBar = findViewById(R.id.myprogressBar);
        new Subject3.JSONDownloader(context).retrieve(binding.gridview, binding.myprogressBar);
        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Subject3.JSONDownloader(context).retrieve(binding.gridview, binding.myprogressBar);
            }
        });

        binding.refreshBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Subject3.JSONDownloader1(context).retrieve(binding.gridview1, binding.myprogressBar1);
            }
        });

        binding.refreshBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Subject3.JSONDownloader2(context).retrieve(binding.gridview2, binding.myprogressBar2);
            }
        });

        binding.Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tablay1.setVisibility(View.VISIBLE);
                binding.tablay2.setVisibility(View.GONE);
                binding.tablay3.setVisibility(View.GONE);

                binding.bar1.setVisibility(View.VISIBLE);
                binding.bar2.setVisibility(View.INVISIBLE);
                binding.bar3.setVisibility(View.INVISIBLE);

                new Subject3.JSONDownloader(context).retrieve(binding.gridview, binding.myprogressBar);
            }
        });

        binding.Videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tablay1.setVisibility(View.GONE);
                binding.tablay2.setVisibility(View.VISIBLE);
                binding.tablay3.setVisibility(View.GONE);

                binding.bar1.setVisibility(View.INVISIBLE);
                binding.bar2.setVisibility(View.VISIBLE);
                binding.bar3.setVisibility(View.INVISIBLE);
                new Subject3.JSONDownloader1(context).retrieve(binding.gridview1, binding.myprogressBar1);
            }
        });

        binding.syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tablay1.setVisibility(View.GONE);
                binding.tablay2.setVisibility(View.GONE);
                binding.tablay3.setVisibility(View.VISIBLE);

                binding.bar1.setVisibility(View.INVISIBLE);
                binding.bar2.setVisibility(View.INVISIBLE);
                binding.bar3.setVisibility(View.VISIBLE);
                new Subject3.JSONDownloader2(context).retrieve(binding.gridview2, binding.myprogressBar2);
            }
        });


    }


    public class PDFDoc {
        int id;
        String name, category, pdfURL, pdfIconURL;

        public int getId() {return id;}
        public void setId(int id) {this.id = id;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getAuthor() {return category;}
        public void setCategory(String category) {this.category = category;}
        public String getPdfURL() {return pdfURL;}
        public void setPdfURL(String pdfURL) {this.pdfURL = pdfURL;}
        //public String getPdfIconURL() {return pdfIconURL;}
        //public void setPdfIconURL(String pdfIconURL) {this.pdfIconURL = pdfIconURL;}
    }

    public class YoutubeLink {
        int id;
        String name, category, youtubeURL;

        public int getId() {return id;}
        public void setId(int id) {this.id = id;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getAuthor() {return category;}
        public void setCategory(String category) {this.category = category;}
        public  String getyoutubeURL() {return youtubeURL;}
        public void setyoutubeURL(String youtubeURL) {this.youtubeURL = youtubeURL;}
    }

    public class PDFDocSyllabus {
        int id;
        String name, category, pdfsyllabusURL, pdfIconURL;

        public int getId() {return id;}
        public void setId(int id) {this.id = id;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getAuthor() {return category;}
        public void setCategory(String category) {this.category = category;}
        public String getPdfsyllabusURL() {return pdfsyllabusURL;}
        public void setPdfsyllabusURL(String pdfsyllabusURL) {this.pdfsyllabusURL = pdfsyllabusURL;}

    }

    public class GridViewAdapter extends BaseAdapter {
        Context c;
        ArrayList<Subject3.PDFDoc> pdfDocuments;

        public GridViewAdapter(Context c,  ArrayList<Subject3.PDFDoc>pdfDocuments) {
            this.c = c;
            this.pdfDocuments = pdfDocuments;
        }



        @Override
        public int getCount() {
            return pdfDocuments.size();
        }

        @Override
        public Object getItem(int pos) {
            return pdfDocuments.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                view = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false);
            }

            TextView txtName = view.findViewById(R.id.pdfNameTxt);
            TextView txtAuthor = view.findViewById(R.id.authorTxt);
            //  ImageView pdfIcon = view.findViewById(R.id.imageview);

            final Subject3.PDFDoc pdfDoc = (Subject3.PDFDoc) this.getItem(pos);
            txtName.setText(pdfDoc.getName());
            txtAuthor.setText(pdfDoc.getAuthor());

          /*  if(pdfDoc.getPdfURL() != null && pdfDoc.getPdfURL().length()> 0) {
                Picasso.get().load(pdfDoc.getPdfIconURL()).placeholder(R.drawable.placeholder).into(pdfIcon);
            } else {
                Picasso.get().load(R.drawable.pdf_icon).into(pdfIcon);
                TastyToast.makeText(context, "Empty Image URL", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TastyToast.makeText(context, pdfDoc.getName(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    Intent i =  new Intent(c, PDFActivity.class);
                    i.putExtra("PATH", pdfDoc.getPdfURL());
                    i.putExtra("NAME", pdfDoc.getName());
                    c.startActivity(i);
                }
            });
            return view;
        }
    }


    public class GridViewAdapter1 extends BaseAdapter {
        Context c;
        ArrayList<Subject3.YoutubeLink> youtubeVideos;

        public GridViewAdapter1(Context c,  ArrayList<Subject3.YoutubeLink>youtubeVideos) {
            this.c = c;
            this.youtubeVideos = youtubeVideos;
        }



        @Override
        public int getCount() {
            return youtubeVideos.size();
        }

        @Override
        public Object getItem(int pos) {
            return youtubeVideos.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                view = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false);
            }

            TextView txtName = view.findViewById(R.id.pdfNameTxt);
            TextView txtAuthor = view.findViewById(R.id.authorTxt);
            //  ImageView pdfIcon = view.findViewById(R.id.imageview);

            final Subject3.YoutubeLink ytlink = (Subject3.YoutubeLink) this.getItem(pos);
            txtName.setText(ytlink.getName());
            txtAuthor.setText(ytlink.getAuthor());

          /*  if(pdfDoc.getPdfURL() != null && pdfDoc.getPdfURL().length()> 0) {
                Picasso.get().load(pdfDoc.getPdfIconURL()).placeholder(R.drawable.placeholder).into(pdfIcon);
            } else {
                Picasso.get().load(R.drawable.pdf_icon).into(pdfIcon);
                TastyToast.makeText(context, "Empty Image URL", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // TO-DO ADD WEBINTENT
                    openYoutubeLink(ytlink.getyoutubeURL());

                }
            });
            return view;
        }
    }

    public class GridViewAdapter2 extends BaseAdapter {
        Context c;
        ArrayList<Subject3.PDFDocSyllabus> pdfsyllabusDocuments;

        public GridViewAdapter2(Context c,  ArrayList<Subject3.PDFDocSyllabus>pdfsyllabusDocuments) {
            this.c = c;
            this.pdfsyllabusDocuments = pdfsyllabusDocuments;
        }



        @Override
        public int getCount() {
            return pdfsyllabusDocuments.size();
        }

        @Override
        public Object getItem(int pos) {
            return pdfsyllabusDocuments.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                view = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false);
            }

            TextView txtName = view.findViewById(R.id.pdfNameTxt);
            TextView txtAuthor = view.findViewById(R.id.authorTxt);
            //  ImageView pdfIcon = view.findViewById(R.id.imageview);

            final Subject3.PDFDocSyllabus pdfDoc = (Subject3.PDFDocSyllabus) this.getItem(pos);
            txtName.setText(pdfDoc.getName());
            txtAuthor.setText(pdfDoc.getAuthor());

          /*  if(pdfDoc.getPdfURL() != null && pdfDoc.getPdfURL().length()> 0) {
                Picasso.get().load(pdfDoc.getPdfIconURL()).placeholder(R.drawable.placeholder).into(pdfIcon);
            } else {
                Picasso.get().load(R.drawable.pdf_icon).into(pdfIcon);
                TastyToast.makeText(context, "Empty Image URL", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TastyToast.makeText(context, pdfDoc.getName(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    Intent i =  new Intent(c, PDFActivity.class);
                    i.putExtra("PATH", pdfDoc.getPdfsyllabusURL());
                    i.putExtra("NAME", pdfDoc.getName());
                    c.startActivity(i);
                }
            });
            return view;
        }
    }


    public class JSONDownloader {
        private static final String PDF_SITE_URL = "https://study-server.xyz/studyroom/BookStore/SemesterTwo/EngineeringChemistry2";
        private final Context c;

        private Subject3.GridViewAdapter adapter;

        public JSONDownloader(Context c){
            this.c = c;
        }

        public void retrieve(final GridView gv, final ProgressBar myProgressBar){
            final  ArrayList<Subject3.PDFDoc>pdfDocuments = new ArrayList<>();

            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);
            AndroidNetworking.get(PDF_SITE_URL)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            Subject3.PDFDoc p;
                            try
                            {
                                for(int i=0;i< response.length();i++)
                                {
                                    jo=response.getJSONObject(i);
                                    int id=jo.getInt("id");
                                    String name=jo.getString("name");
                                    String category=jo.getString("category");
                                    String description=jo.getString("description");
                                    String pdfURL=jo.getString("pdf_url");
                                    //String pdfIconURL=jo.getString("pdf_icon_url");

                                    p = new Subject3.PDFDoc();
                                    p.setId(id);
                                    p.setName(name);
                                    p.setCategory(category);
                                    p.setCategory(description);
                                    p.setPdfURL(PDF_SITE_URL+pdfURL);
                                    // p.setPdfIconURL(PDF_SITE_URL+pdfIconURL);

                                    pdfDocuments.add(p);
                                }
                                adapter = new Subject3.GridViewAdapter(c, pdfDocuments);
                                gv.setAdapter(adapter);
                                myProgressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                myProgressBar.setVisibility(View.GONE);
                                //TastyToast.makeText(context, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED. "+e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            myProgressBar.setVisibility(View.GONE);
                            //TastyToast.makeText(context, "UNSUCCESSFUL: ERROR IS: "+anError.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    });
        }

    }

    public class JSONDownloader1 {
        private static final String PDF_SITE_URL = "https://study-server.xyz/studyroom/BookStore/SemesterTwo/EngineeringChemistry2/Youtube";
        private final Context c1;

        private Subject3.GridViewAdapter1 adapter1;

        public JSONDownloader1(Context c1){
            this.c1 = c1;
        }

        public void retrieve(final GridView gv1, final ProgressBar myProgressBar1){
            final  ArrayList<Subject3.YoutubeLink> youtubeVideos = new ArrayList<>();

            myProgressBar1.setIndeterminate(true);
            myProgressBar1.setVisibility(View.VISIBLE);
            AndroidNetworking.get(PDF_SITE_URL)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            Subject3.YoutubeLink p;
                            try
                            {
                                for(int i=0;i< response.length();i++)
                                {
                                    jo=response.getJSONObject(i);
                                    int id=jo.getInt("id");
                                    String name=jo.getString("name");
                                    String category=jo.getString("category");
                                    String description=jo.getString("description");
                                    String youtubeURL = jo.getString("yt_url");
                                    //String pdfURL=jo.getString("pdf_url");
                                    //String pdfIconURL=jo.getString("pdf_icon_url");

                                    p = new Subject3.YoutubeLink();
                                    p.setId(id);
                                    p.setName(name);
                                    p.setCategory(category);
                                    p.setCategory(description);
                                    p.setyoutubeURL(youtubeURL);
                                    //   p.setPdfURL(PDF_SITE_URL+pdfURL);
                                    // p.setPdfIconURL(PDF_SITE_URL+pdfIconURL);

                                    youtubeVideos.add(p);
                                }
                                adapter1 = new Subject3.GridViewAdapter1(c1, youtubeVideos);
                                gv1.setAdapter(adapter1);
                                myProgressBar1.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                myProgressBar1.setVisibility(View.GONE);
                                //TastyToast.makeText(context, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED. "+e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            myProgressBar1.setVisibility(View.GONE);
                            //TastyToast.makeText(context, "UNSUCCESSFUL: ERROR IS: "+anError.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    });
        }

    }


    public class JSONDownloader2 {
        private static final String PDF_SITE_URL = "https://study-server.xyz/studyroom/BookStore/SemesterTwo/EngineeringChemistry2/Syllabus";
        private final Context c2;

        private Subject3.GridViewAdapter2 adapter;

        public JSONDownloader2(Context c){
            this.c2 = c;
        }

        public void retrieve(final GridView gv, final ProgressBar myProgressBar){
            final  ArrayList<Subject3.PDFDocSyllabus>pdfsyllabusDocuments = new ArrayList<>();

            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);
            AndroidNetworking.get(PDF_SITE_URL)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            Subject3.PDFDocSyllabus p;
                            try
                            {
                                for(int i=0;i< response.length();i++)
                                {
                                    jo=response.getJSONObject(i);
                                    int id=jo.getInt("id");
                                    String name=jo.getString("name");
                                    String category=jo.getString("category");
                                    String description=jo.getString("description");
                                    String pdfsyllabusURL=jo.getString("pdf_url");
                                    //String pdfIconURL=jo.getString("pdf_icon_url");

                                    p = new Subject3.PDFDocSyllabus();
                                    p.setId(id);
                                    p.setName(name);
                                    p.setCategory(category);
                                    p.setCategory(description);
                                    p.setPdfsyllabusURL(PDF_SITE_URL+pdfsyllabusURL);
                                    // p.setPdfIconURL(PDF_SITE_URL+pdfIconURL);

                                    pdfsyllabusDocuments.add(p);
                                }
                                adapter = new Subject3.GridViewAdapter2(c2, pdfsyllabusDocuments);
                                gv.setAdapter(adapter);
                                myProgressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                myProgressBar.setVisibility(View.GONE);
                                //TastyToast.makeText(context, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED. "+e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            myProgressBar.setVisibility(View.GONE);
                            //TastyToast.makeText(context, "UNSUCCESSFUL: ERROR IS: "+anError.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    });
        }

    }
    private void openYoutubeLink(String youtubeUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.youtube");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // YouTube app is not installed, open in web browser
            intent.setPackage(null);
            context.startActivity(intent);
        }
    }
}