package com.example.leona.musicon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<String> albuns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("MusicOn", 0);
        Set<String> AlbumSet = sp.getStringSet("MusicKey", new HashSet<String>());

        albuns = new ArrayList<String>(AlbumSet);

        /*albuns.add("ACDC | Highway To Hell | AA | 1979 | 4 Estrelas ");
        albuns.add("Linkin Park | Meteora  | BB | 2003 | 3 Estrelas ");
        albuns.add("Hardwell | United We Are | CC | 2015 | 5 Estrelas ");
        albuns.add("David Fonseca | Futuro Eu | DD |2015 | 3 Estrelas ");
        albuns.add("Queen | Made in Heaven | EE | 1995 | 5 Estrelas ");*/

        ListView listView = (ListView) findViewById(R.id.listView_musicas);

        SimpleAdapter adapter = createSimpleAdapter(albuns);

        listView.setAdapter(adapter);

        Spinner s = (Spinner) findViewById(R.id.pesquisa);

        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.pesquisa, android.R.layout.simple_spinner_item);

        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(adapterS);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialog_confirmar, null));

// Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ListView listView = (ListView) findViewById(R.id.listView_musicas);

                        albuns.remove(position);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, albuns);
                        listView.setAdapter(adapter);

                        // User clicked OK button
                        Toast.makeText(MainActivity.this, R.string.Apagado1, Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(MainActivity.this, R.string.Cancelar, Toast.LENGTH_SHORT).show();

                    }
                });


// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }


        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(MainActivity.this, R.string.Guardar1, Toast.LENGTH_SHORT).show();
        // Guardar os contactos para as shared preferences
        SharedPreferences sp = getSharedPreferences("MusicOn", 0);
        SharedPreferences.Editor editor = sp.edit();

        HashSet AlbumSet = new HashSet(albuns);

        editor.putStringSet("MusicKey", AlbumSet);
        editor.commit();
    }

    private SimpleAdapter createSimpleAdapter(ArrayList<String> albuns)
    {

        List<HashMap<String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();

        for (String c : albuns)
        {
            HashMap<String, String> hashMap = new HashMap<>();
            String[] split = c.split(" \\| ");
            hashMap.put(getString(R.string.Artista1), split[0]);
            hashMap.put(getString(R.string.Album1), split[1]);
            hashMap.put(getString(R.string.Editora), split[2]);
            hashMap.put(getString(R.string.Ano1), split[3]);
            hashMap.put(getString(R.string.Classificação), split[4]);
            simpleAdapterData.add(hashMap);
        }
        String[] from = {"Artista", "Álbum", "Editora", "Ano", "Classificação"};
        int[] to = {R.id.textView_Album, R.id.textView_Artista, R.id.textView_Editora, R.id.textView_Ano, R.id.textView_Classificacao};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview_albuns, from, to);
        return simpleAdapter;

    }

    public void onClick_search(View view)
    {
        EditText et = (EditText) findViewById(R.id.texto);
        Spinner sp = (Spinner) findViewById(R.id.pesquisa);
        ListView lv = (ListView) findViewById(R.id.listView_musicas);

        String termo = et.getText().toString();

        if(termo.equals(""))
        {
            SimpleAdapter adapter = createSimpleAdapter(albuns);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.Mostrar2, Toast.LENGTH_SHORT).show();
        }
        else
        {
            String itemSeleccionado = (String) sp.getSelectedItem();

            ArrayList<String> resultados = new ArrayList<>();


            if (itemSeleccionado.equals(getString(R.string.Todos1))) {
                for (int i = 0; i < albuns.size(); i++)
                {
                    String c = albuns.get(i);

                    boolean contem = c.contains(termo);

                    if (contem)
                    {
                        resultados.add(c);
                    }
                }
            }
            else if (itemSeleccionado.equals(getString(R.string.Artista1)))
            {
                for (int i = 0; i < albuns.size(); i++)
                {
                    String c = albuns.get(i);

                    String[] s = c.split("\\|");
                    String name = s[0];

                    boolean contem = name.contains(termo);

                    if (contem)
                    {
                        resultados.add(c);
                    }
                }
            }
            else if (itemSeleccionado.equals(getString(R.string.Album1)))
            {
                for (int i = 0; i < albuns.size(); i++)
                {
                    String c = albuns.get(i);

                    String[] s = c.split("\\|");
                    String number = s[1];

                    boolean contem = number.contains(termo);

                    if (contem)
                    {
                        resultados.add(c);
                    }
                }
            }
            else if (itemSeleccionado.equals(getString(R.string.Editora)))
            {
                for (int i = 0; i < albuns.size(); i++)
                {
                    String c = albuns.get(i);

                    String[] s = c.split("\\|");
                    String number = s[2];

                    boolean contem = number.contains(termo);

                    if (contem)
                    {
                        resultados.add(c);
                    }
                }
            }
            else if (itemSeleccionado.equals(getString(R.string.Ano1)))
            {
                for (int i = 0; i < albuns.size(); i++)
                {
                    String c = albuns.get(i);

                    String[] s = c.split("\\|");
                    String number = s[3];

                    boolean contem = number.contains(termo);

                    if (contem)
                    {
                        resultados.add(c);
                    }
                }
            }
            else if (itemSeleccionado.equals(getString(R.string.Classificação)))
            {
                for (int i = 0; i < albuns.size(); i++)
                {
                    String c = albuns.get(i);

                    String[] s = c.split("\\|");
                    String number = s[4];

                    boolean contem = number.contains(termo);

                    if (contem)
                    {
                        resultados.add(c);
                    }
                }
            }

            boolean vazia = resultados.isEmpty();

            if(vazia == false)
            {
                SimpleAdapter adapter = createSimpleAdapter(resultados);
                lv.setAdapter(adapter);
                Toast.makeText(MainActivity.this, R.string.Mostrar1, Toast.LENGTH_SHORT).show();
            }
            else
            {
                SimpleAdapter adapter = createSimpleAdapter(albuns);
                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, R.string.Resultados1, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick_add(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.adicionar, null));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                AlertDialog al = (AlertDialog) dialog;

                EditText etArtista = (EditText) al.findViewById(R.id.editText_Artista);
                EditText etAlbum = (EditText) al.findViewById(R.id.editText_Album);
                EditText etEditora = (EditText) al.findViewById(R.id.editText_Editora);
                EditText etAno = (EditText) al.findViewById(R.id.editText_Ano);
                RatingBar rbEstrela = (RatingBar) al.findViewById(R.id.ratingBar);


                rbEstrela.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        Toast.makeText(MainActivity.this, String.valueOf(rating), Toast.LENGTH_SHORT).show();

                    }
                });

                String Artista = etArtista.getText().toString();
                String Album = etAlbum.getText().toString();
                String Editora = etEditora.getText().toString();
                String Ano = etAno.getText().toString();
                String Estrela = "" + (int) rbEstrela.getRating();



                if (Artista.equals("") || Album.equals("") || Ano.equals("")) {

                    Toast.makeText(MainActivity.this, R.string.Inserir1, Toast.LENGTH_SHORT).show();



                }else {

                    List<HashMap<String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();

                    HashMap<String, String> hashMap = new HashMap<>();



                    hashMap.put("Artista",  Artista);
                    hashMap.put("Álbum",  Album);
                    hashMap.put("Editora",  Editora);
                    hashMap.put("Ano",  Ano);
                    hashMap.put("Classificação",  Estrela);
                    simpleAdapterData.add(hashMap);

                    String[] from = {"Artista", "Álbum", "Editora", "Ano", "Classificação"};
                    int[] to = {R.id.textView_Album, R.id.textView_Artista, R.id.textView_Editora, R.id.textView_Ano, R.id.textView_Classificacao};
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview_albuns, from, to);

                    ListView listView = (ListView) findViewById(R.id.listView_musicas);
                    listView.setAdapter(simpleAdapter);





                   /* String musicas = Artista + " | " + Album + " | " + Editora + " | " + Ano + " | " + Estrela + " Estrelas";

                    albuns.add(musicas);

                    SimpleAdapter adapter = createSimpleAdapter(albuns);

                    ListView listView = (ListView) findViewById(R.id.listView_musicas);
                    listView.setAdapter(adapter);*/
                    Toast.makeText(MainActivity.this, R.string.Adicionado1, Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(MainActivity.this, R.string.Não1, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setTitle(R.string.Novo1);

        builder.setMessage(R.string.Introduzir);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

