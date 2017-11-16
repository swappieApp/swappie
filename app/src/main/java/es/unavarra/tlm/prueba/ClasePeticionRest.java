package es.unavarra.tlm.prueba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import android.support.v4.widget.DrawerLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import es.unavarra.tlm.prueba.PantallaPrincipal.AdaptadorProductos;
import es.unavarra.tlm.prueba.chat.ChatActivity;
import es.unavarra.tlm.prueba.model.Producto;
import es.unavarra.tlm.prueba.PantallaPrincipal.Navigation_drawer;
import es.unavarra.tlm.prueba.PantallaPrincipal.SwipeStackCardListener;
import es.unavarra.tlm.prueba.PantallaPrincipal.UsuarioRegistrado;
import es.unavarra.tlm.prueba.model.Objeto;
import link.fls.swipestack.SwipeStack;

/**
 * Created by ibai on 10/20/17.
 */

public class ClasePeticionRest {


        /****************************************/
       /*                                      */
      /*    CLASES ÚTILES (POR EL MOMENTO)    */
     /*                                      */
    /****************************************/


    public static ArrayList<KeyValue> peticionRest(final ArrayList<KeyValue> parametros, final String funcionAPI, final String metodo){

        final ArrayList<KeyValue> respuesta = new ArrayList<>();

        try {

            respuesta.clear();

            Log.d("etiqueta", "size_params = "+parametros.size());

            String urlParametros = "";
            if (parametros != null) {
                if (parametros.size() != 0) {
                    urlParametros = URLEncoder.encode(parametros.get(0).getKey(), "utf-8") + "=" + URLEncoder.encode(parametros.get(0).getValue(), "utf-8");
                    for (int x = 1; x < parametros.size(); x++) {
                        urlParametros += "&" + URLEncoder.encode(parametros.get(x).getKey(), "utf-8") + "=" + URLEncoder.encode(parametros.get(x).getValue(), "utf-8");
                    }
                }
            }

            String stringURL = "https://aux.swappie.tk/api/" + funcionAPI + ".php?"+urlParametros;
            URL url = new URL(stringURL);

            Log.d("etiqueta", String.valueOf(url));

            final HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
            myConnection.setInstanceFollowRedirects(false);

            if (metodo.equals("post")){
                myConnection.setDoOutput(true);
            }

            if (myConnection.getResponseCode() == 200){

                Log.d("etiqueta", "entro al if");
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.setLenient(true);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    String value = jsonReader.nextString();

                    respuesta.add(new KeyValue(key, value));
                }
                jsonReader.close();

            } else {
                Log.d("etiqueta", "entro al else");
                respuesta.add(new KeyValue("ok", "false"));
                respuesta.add(new KeyValue("error", "error en la peticion"));
            }

            myConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("etiqueta", "SALGO");
        return respuesta;

    }

    private static ArrayList<KeyValue> doFileUpload(File foto) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String urlString = "https://aux.swappie.tk/api/guardar_foto.php";

        try {

            Log.d("etiqueta", "URI:"+foto.getAbsolutePath());
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(foto);
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + foto.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }

        //------------------ read the SERVER RESPONSE
        try {

            inStream = new DataInputStream(conn.getInputStream());
            String str;

            ArrayList<KeyValue> respuesta = new ArrayList<>();
            InputStreamReader responseBodyReader = new InputStreamReader(inStream, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.setLenient(true);
            jsonReader.beginObject(); // Start processi ng the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                String value = jsonReader.nextString();
                respuesta.add(new KeyValue(key, value));
            }
            jsonReader.close();

            /*while ((str = inStream.readLine()) != null) {

                Log.e("Debug", "Server Response " + str);

            }*/

            inStream.close();

            return respuesta;

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return null;
    }

    public static class GuardarUsuario extends AsyncTask<String, String, Integer> {

        String funcionAPI = "guardar_usuario";
        String nombre, apellidos, email, metodoLogin;
        ProgressDialog dialog;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public GuardarUsuario(Activity activity, String nombre, String apellidos, String email, String password, String ubicacion, String metodoLogin) {

            this.activity = activity;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.metodoLogin = metodoLogin;

            parametros.add(new KeyValue("nombre", nombre));
            parametros.add(new KeyValue("apellidos", apellidos));
            parametros.add(new KeyValue("metodo_login", metodoLogin));
            parametros.add(new KeyValue("email", email));
            if (password != null) {
                parametros.add(new KeyValue("password", password));
            }
            parametros.add(new KeyValue("ubicacion", ubicacion));
            this.dialog = new ProgressDialog(activity);
            this.dialog.setMessage("Please wait");
            this.dialog.show();

        }


        @Override
        protected Integer doInBackground(String... strings) {

            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");

            int idUsuario = 0;

            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                if (respuesta.get(1).getKey().equals("id_usuario")){
                    idUsuario = Integer.parseInt(respuesta.get(1).getValue());
                }
            }
            return idUsuario;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            guardarUsuarioEnSharedPreferences(activity, result, metodoLogin, nombre, apellidos, email);
            if (result==0){
                this.dialog.dismiss();
                mostrarToast(activity, "Email ya registrado");

                //Boramos las SharedPreferences
                SharedPreferences info = activity.getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.clear();
                // Se borra la sesión de Shared Preferences.
                editor.putBoolean("sesion", false);
                editor.commit();

            }else{
                mostrarToast(activity, "Creado usuario Nº "+result);
                Intent intent = new Intent(activity, UsuarioRegistrado.class);
                activity.startActivity(intent);
                (activity).finish();
            }
        }

    }

    public static class GuardarSwipe extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_swipe";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;
        int idUsuario;
        boolean decision;


        public GuardarSwipe(Activity activity, int idUsuario, int idObjeto, boolean decision) {
            parametros.add(new KeyValue("id_usuario1", idUsuario+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            parametros.add(new KeyValue("decision", decision+""));
            this.activity = activity;
            this.idUsuario = idUsuario;
            this.decision=decision;
            //Log.d("variables2", String.valueOf(this.idUsuario));
        }

        @Override
        protected String doInBackground(String... strings) {
            //Añadir que además si la decision es true, abra la pantalla de registro
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                new CogerObjetoSwipe(activity, idUsuario).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new ComprobarSwipe(activity, Integer.parseInt(parametros.get(0).getValue()), Integer.parseInt(parametros.get(1).getValue())).executeOnExecutor(THREAD_POOL_EXECUTOR);
            }else{
                mostrarToast(activity, "Error al guardar el swipe: " + result);
            }
        }

    }

    public static class GuardarMatch extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_match";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public GuardarMatch(Activity activity, int idUsuario1, int idUsuario2, boolean chatEmpezado) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_objeto", idUsuario2+""));
            parametros.add(new KeyValue("chat_empezado", chatEmpezado+""));
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast(activity, "Match guardado correctamente");
            }else{
                mostrarToast(activity, "Error al guardar el match");
            }
        }

    }

    public static class CrearChat extends AsyncTask<String, String, String> {

        String funcionAPI = "crear_chat";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public CrearChat(Activity activity, int idUsuario1, int idObjeto) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast(activity, "Chat creado correctamente");
            }else{
                mostrarToast(activity, "Error al crear el chat");
            }
        }

    }

    public static class GuardarObjeto extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_objeto";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        File foto;
        Activity activity;

        public GuardarObjeto(Activity activity, int idUsuario, String descripcion, File foto) {
            parametros.add(new KeyValue("id_usuario", idUsuario+""));
            parametros.add(new KeyValue("descripcion", descripcion));
            this.foto = foto;
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {

            int idObjeto = guardarFoto(foto);
            Log.e("etiqueta", "IDFoto = "+idObjeto);
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast(activity, "Objeto guardado correctamente");
                Intent intent = new Intent(activity, UsuarioRegistrado.class);
                activity.startActivity(intent);
                activity.finish();
            }else{
                mostrarToast(activity, "Error al guardar el objeto");
            }
        }

    }

    public static class ComprobarSwipe extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_swipe";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;
        PopupWindow popUpWindow;

        public ComprobarSwipe(Activity activity, int idUsuario1, int idObjeto) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                if (result.get(1).getValue().equals("true")){
                    new GuardarMatch(activity, Integer.parseInt(parametros.get(0).getValue()), Integer.parseInt(parametros.get(1).getValue()), true).executeOnExecutor(THREAD_POOL_EXECUTOR);
                    new CrearChat(activity, Integer.parseInt(parametros.get(0).getValue()), Integer.parseInt(parametros.get(1).getValue())).executeOnExecutor(THREAD_POOL_EXECUTOR);
                    mostrarToast(activity, "MATCH!");

                    //CARGA POPUP MATCH
                    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.match, (ViewGroup) activity.findViewById(R.id.match_element));

                    popUpWindow = new PopupWindow(layout, DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT, true);
                    popUpWindow.showAtLocation(layout, Gravity.TOP, 0, 0);

                    //BOTÓN PARA SEGUIR HACIENDO SWIPE, CIERRA EL POPUP
                    Button cancelButton = (Button) layout.findViewById(R.id.buttonSeguir);
                    cancelButton.setOnClickListener(cancel_button_click_listener);

                    //AQUÍ VA EL BOTON PARA ABRIR CHAT, AÚN NO HACE NADA
                    Button buttonIniciarChat = (Button) layout.findViewById(R.id.buttonChat);
                    buttonIniciarChat.setOnClickListener(button_chat_click_listener);



                }else if (result.get(1).getValue().equals("false")){
                    //mostrarToast(activity, "NO MATCH!");
                }
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "ERROR: " + result.get(1).getValue());
            }
        }

        private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
            public void onClick(View v) {
                popUpWindow.dismiss();
            }
        };

        private View.OnClickListener button_chat_click_listener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChatActivity.class);
                activity.startActivity(intent);
            }
        };

    }

    public static class HacerLogin extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "hacer_login";
        String nombre, apellidos, email, metodoLogin;
        ProgressDialog dialog;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public HacerLogin(Activity activity, String metodoLogin, String email, String password) {

            parametros.add(new KeyValue("metodo_login", metodoLogin));
            parametros.add(new KeyValue("email", email));
            parametros.add(new KeyValue("password", password));

            this.activity = activity;
            this.email=email;
            this.metodoLogin="email";

            this.dialog = new ProgressDialog(activity);
            this.dialog.setMessage("Please wait");
            this.dialog.show();

        }

        public HacerLogin(Activity activity, String metodoLogin, String email) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("metodo_login", metodoLogin));
            parametros.add(new KeyValue("email", email));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.size()==0){
                Log.d("etiqueta","nulo1");
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);

            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                int idUsuario = Integer.parseInt(result.get(1).getValue());
                this.nombre = result.get(2).getValue();
                this.apellidos = result.get(3).getValue();

                mostrarToast(activity, "Logueado usuario Nº " + result.get(1).getValue());

                guardarUsuarioEnSharedPreferences(activity, Integer.parseInt(result.get(1).getValue()), metodoLogin, nombre, apellidos, email);
                Intent intent = new Intent(activity, UsuarioRegistrado.class);
                activity.startActivity(intent);
                (activity).finish();

            }else if (result.get(1).getKey().equals("error")){
                this.dialog.dismiss();
                mostrarToast(activity, "ERROR: " + result.get(1).getValue());
            }
        }

    }

    public static class CogerObjetosInicio extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_objetos_inicio";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public CogerObjetosInicio(Activity activity, String idUsuario) {
            parametros.add(new KeyValue("id_usuario", idUsuario));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());

                if (!result.get(1).getValue().equals("[]")){
                    Gson gson = new Gson();
                    Log.e("etiqueta", result.get(1).getValue());
                    Objeto[] objetos = gson.fromJson(result.get(1).getValue(), Objeto[].class);
                    new CargarDatos(objetos, activity).executeOnExecutor(THREAD_POOL_EXECUTOR);
                }else{
                    Log.e("etiqueta", "No hay objetos");
                }

            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class CogerObjetoSwipe extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_objeto_swipe";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;
        RelativeLayout rel;

        public CogerObjetoSwipe(Activity activity, int idUsuario) {
            parametros.add(new KeyValue("id_usuario", idUsuario+""));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                //mostrarToast(activity, "JSON: " + result.get(1).getValue());

                if (!result.get(1).getValue().equals("[]")){
                    Gson gson = new Gson();
                    Log.e("etiqueta", result.get(1).getValue());
                    rel= (RelativeLayout) activity.findViewById(R.id.loading);
                    rel.setVisibility(View.GONE);
                    Objeto objeto = gson.fromJson(result.get(1).getValue(), Objeto.class);
                    new CargarObjetoNuevo(objeto, activity).executeOnExecutor(THREAD_POOL_EXECUTOR);
                }else{
                    Log.e("etiqueta", "No hay objetos nuevos");
                }

            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class CogerObjetoAleatorioSwipe extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_objeto_aleatorio_swipe";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;
        RelativeLayout rel;

        public CogerObjetoAleatorioSwipe(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                if (!result.get(1).getValue().equals("[]")){
                    Gson gson = new Gson();
                    Log.e("etiqueta", result.get(1).getValue());
                    rel= (RelativeLayout) activity.findViewById(R.id.loading);
                    rel.setVisibility(View.GONE);
                    Objeto objeto = gson.fromJson(result.get(1).getValue(), Objeto.class);
                    new CargarObjetoAleatorioNuevo(objeto, activity).executeOnExecutor(THREAD_POOL_EXECUTOR);
                }else{
                    Log.e("etiqueta", "No hay objetos nuevos");
                }

            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class CogerObjetosAleatoriosInicio extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_objetos_aleatorios";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;
        RelativeLayout rel;

        public CogerObjetosAleatoriosInicio(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                //mostrarToast(activity, "JSON: " + result.get(1).getValue());

                Gson gson = new Gson();
                Objeto[] objetos = gson.fromJson(result.get(1).getValue(), Objeto[].class);
                new CargarDatosNoRegistrado(objetos, activity).executeOnExecutor(THREAD_POOL_EXECUTOR);
                rel= (RelativeLayout) activity.findViewById(R.id.loading);
                rel.setVisibility(View.GONE);

            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class ComprobarFacebook extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_facebook";
        String nombre, apellidos, email, metodoLogin,password,ubicacion;
        int id;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public ComprobarFacebook(Activity activity, String email) {
            parametros.add(new KeyValue("email", email));
            this.activity = activity;
            this.email=email;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.size()==0){
                Log.d("etiquetafb","nulo1");
            }

            for (int i=0;i<respuesta.size();i++){
                Log.d("etiquetafb",respuesta.get(i).getKey());
                Log.d("etiquetafb",respuesta.get(i).getValue());
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);

            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                Log.d("etiquetafb","entro al if");

                this.id = Integer.parseInt(result.get(1).getValue());

                this.guardarUsuarioEnSharedPreferences(Integer.parseInt(result.get(1).getValue()));

                mostrarToast(activity, "Logueado usuario Nº " + result.get(1).getValue());


            }else if (result.get(1).getKey().equals("error")){

                Log.d("etiquetafb","entro al else");

                if (result.get(1).getValue().equals("no registrado")){

                    Log.d("etiquetafb","entro a no registrado");


                    SharedPreferences settings = activity.getSharedPreferences("Config", 0);
                    String name=settings.getString("nombre","");
                    String apellidos= settings.getString("apellidos","");
                    this.nombre=name;
                    this.apellidos=apellidos;
                    this.password="";
                    this.ubicacion=GetLocation.getCoords(activity);
                    this.metodoLogin="facebook";

                    new ClasePeticionRest.GuardarUsuario(this.activity,this.nombre,this.apellidos,this.email,this.password,this.ubicacion,this.metodoLogin).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
                mostrarToast(activity, "ERROR: " + result.get(1).getValue());
            }
        }

        public void guardarUsuarioEnSharedPreferences(int id){

            ClasePeticionRest.guardaridUsuario(activity,id);

                Intent intent = new Intent(activity, UsuarioRegistrado.class);
                   activity.startActivity(intent);
                (activity).finish();

        }

    }

    public static class ComprobarGoogle extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_google";
        String nombre, apellidos, email, metodoLogin,password,ubicacion;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;


        public ComprobarGoogle(Activity activity, String nombre, String apellidos, String email, String ubicacion) {

            parametros.add(new KeyValue("email", email));
            this.activity = activity;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.ubicacion = ubicacion;
            this.email=email;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);

            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                int idUsuario = Integer.parseInt(result.get(1).getValue());

                this.guardarUsuarioEnSharedPreferences(Integer.parseInt(result.get(1).getValue()));
                mostrarToast(activity, "Logueado usuario Nº " + result.get(1).getValue());


            }else if (result.get(1).getKey().equals("error")){
                if (result.get(1).getValue().equals("no registrado")){

                    SharedPreferences settings = activity.getSharedPreferences("Config", 0);
                    String name=settings.getString("nombre","");
                    String apellidos= settings.getString("apellidos","");
                    this.nombre=name;
                    this.apellidos=apellidos;
                    this.password="";
                    this.ubicacion=GetLocation.getCoords(activity);
                    this.metodoLogin="google";

                    new ClasePeticionRest.GuardarUsuario(this.activity,this.nombre,this.apellidos,this.email,this.password,this.ubicacion,this.metodoLogin).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }else{
                    mostrarToast(activity, "ERROR: " + result.get(1).getValue());
                }
            }
        }

        public void guardarUsuarioEnSharedPreferences(int id){

            ClasePeticionRest.guardaridUsuario(activity,id);

            Intent intent = new Intent(activity, UsuarioRegistrado.class);
            activity.startActivity(intent);
            (activity).finish();
        }


    }





        /************************/
       /*                      */
      /*    MÉTODOS VARIOS    */
     /*                      */
    /************************/


    public static class KeyValue{

        String key, value;

        public KeyValue(String key, String value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static void mostrarToast(final Activity activity, final String frase){
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, frase, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void mostrarCustomToast(final Activity activity){
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {

                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.toast, (ViewGroup) activity.findViewById(R.id.match_toast));
                Toast toast = new Toast(activity);
                toast.setView(layout);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 230);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public static void guardarUsuarioEnSharedPreferences(Activity activity, int id, String metodoLogin, String nombre, String apellidos, String email){

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = settings.edit();

        Log.e("etiqueta", "GUARDAR_ID:"+id);
        editor.putInt("id", id);
        editor.putString("metodo", metodoLogin);
        editor.putBoolean("sesion", true);
        editor.putString("nombre",nombre);
        editor.putString("apellidos",apellidos);
        editor.putString("email",email);

        editor.commit();

    }

    public static void guardaridUsuario(Activity activity, int id){

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = settings.edit();
;
        editor.putInt("id", id);

        editor.commit();

    }

    public static int guardarFoto(File foto) {

        ArrayList<KeyValue> respuesta = doFileUpload(foto);
        int idObjeto = 0;
        if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")) {
            if (respuesta.get(1).getKey().equals("id_objeto")) {
                idObjeto = Integer.parseInt(respuesta.get(1).getValue());
            }
        }
        return idObjeto;

    }

    public static class CargarDatos extends AsyncTask<String, String, ArrayList<Producto>>{

        Objeto[] objetos;
        Activity activity;
        SwipeStack pilaCartas;

        public CargarDatos(Objeto[] objetos, Activity activity){
            super();
            this.objetos = objetos;
            this.activity = activity;
            pilaCartas = (SwipeStack) activity.findViewById(R.id.pila_cartas);
        }

        @Override
        protected ArrayList<Producto> doInBackground(String... strings) {

            ArrayList<Producto> productos = UsuarioRegistrado.productos;
            for (int x = 0; x < objetos.length; x++){
                Bitmap b = downloadBitmap(objetos[x].getId());
                productos.add(new Producto(b, objetos[x].getDescripcion(), "", Integer.parseInt(objetos[x].getId())));
            }

            return productos;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Producto> productos) {
            super.onPostExecute(productos);

            AdaptadorProductos adaptadorProductos = new AdaptadorProductos(activity, productos);
            pilaCartas.setAdapter(adaptadorProductos);
            pilaCartas.setListener(new SwipeStackCardListener(activity, productos));
            adaptadorProductos.notifyDataSetChanged();

        }

    }

    public static class CargarDatosNoRegistrado extends AsyncTask<String, String, ArrayList<Producto>>{

        Objeto[] objetos;
        Activity activity;
        SwipeStack pilaCartas;

        public CargarDatosNoRegistrado(Objeto[] objetos, Activity activity){
            super();
            this.objetos = objetos;
            this.activity = activity;
            pilaCartas = (SwipeStack) activity.findViewById(R.id.pila_cartas);
        }

        @Override
        protected ArrayList<Producto> doInBackground(String... strings) {

            ArrayList<Producto> productos = Navigation_drawer.productos;
            for (int x = 0; x < objetos.length; x++){
                Bitmap b = downloadBitmap(objetos[x].getId());
                productos.add(new Producto(b, objetos[x].getDescripcion(), "", Integer.parseInt(objetos[x].getId())));
            }

            return productos;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Producto> productos) {
            super.onPostExecute(productos);

            AdaptadorProductos adaptadorProductos = new AdaptadorProductos(activity, productos);
            pilaCartas.setAdapter(adaptadorProductos);
            pilaCartas.setListener(new SwipeStackCardListener(activity, productos));
            adaptadorProductos.notifyDataSetChanged();

        }

    }

    public static class CargarObjetoNuevo extends AsyncTask<String, String, ArrayList<Producto>>{

        Objeto objeto;
        Activity activity;
        SwipeStack pilaCartas;

        public CargarObjetoNuevo(Objeto objeto, Activity activity){
            super();
            this.objeto = objeto;
            this.activity = activity;
            pilaCartas = (SwipeStack) activity.findViewById(R.id.pila_cartas);
        }

        @Override
        protected ArrayList<Producto> doInBackground(String... strings) {

            ArrayList<Producto> productos = UsuarioRegistrado.productos;

            Bitmap b = downloadBitmap(objeto.getId());
            productos.add(new Producto(b, objeto.getDescripcion(), "", Integer.parseInt(objeto.getId())));

            return productos;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Producto> productos) {
            super.onPostExecute(productos);

            AdaptadorProductos adaptadorProductos = new AdaptadorProductos(activity, productos);
            pilaCartas.setAdapter(adaptadorProductos);
            pilaCartas.setListener(new SwipeStackCardListener(activity, productos));
            adaptadorProductos.notifyDataSetChanged();

        }

    }

    public static class CargarObjetoAleatorioNuevo extends AsyncTask<String, String, ArrayList<Producto>>{

        Objeto objeto;
        Activity activity;
        SwipeStack pilaCartas;

        public CargarObjetoAleatorioNuevo(Objeto objeto, Activity activity){
            super();
            this.objeto = objeto;
            this.activity = activity;
            pilaCartas = (SwipeStack) activity.findViewById(R.id.pila_cartas);
        }

        @Override
        protected ArrayList<Producto> doInBackground(String... strings) {

            Bitmap b = downloadBitmap(objeto.getId());
            Navigation_drawer.productos.add(new Producto(b, objeto.getDescripcion(), "", Integer.parseInt(objeto.getId())));

            return Navigation_drawer.productos;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Producto> productos) {
            super.onPostExecute(productos);

            AdaptadorProductos adaptadorProductos = new AdaptadorProductos(activity, Navigation_drawer.productos);
            pilaCartas.setAdapter(adaptadorProductos);
            pilaCartas.setListener(new SwipeStackCardListener(activity, Navigation_drawer.productos));
            adaptadorProductos.notifyDataSetChanged();

        }

    }

    public static Bitmap downloadBitmap(String id){

        Bitmap bmp =null;
        try{
            URL ulrn = new URL("https://aux.swappie.tk/api/img/fotos_objetos/" + id + ".jpg");
            Log.e("etiqueta", "URL:"+ulrn.toString());
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            con.setUseCaches(true);
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
                return bmp;

        }catch(Exception e){}
        return bmp;

    }





        /******************************************/
       /*                                        */
      /*    CLASES INÚTILES (POR EL MOMENTO)    */
     /*                                        */
    /******************************************/


    public static class CogerSwipes extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_swipes";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public CogerSwipes(Activity activity, int idUsuario1) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast(activity, "\"JSON: \" + result.get(1).getValue()");
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "\"JSON: \" + result.get(1).getValue()");
            }

        }

    }

    public static class CogerInfoObjeto extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_info_objeto";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public CogerInfoObjeto(Activity activity, int idObjeto) {
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class ActualizarChat extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "actualizar_chat";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public ActualizarChat(Activity activity, int idUsuario, int idChat) {
            parametros.add(new KeyValue("id_usuario", idUsuario + ""));
            parametros.add(new KeyValue("id_chat", idChat + ""));
            this.activity = activity;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast(activity, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class GuardarMensaje extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_mensaje";

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public GuardarMensaje(Activity activity, int idChat, int idAutor, String mensaje) {
            parametros.add(new KeyValue("id_chat", idChat+""));
            parametros.add(new KeyValue("id_autor", idAutor+""));
            parametros.add(new KeyValue("mensaje", mensaje));
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast(activity, "Mensaje guardado correctamente");
            }else{
                mostrarToast(activity, "Error al guardar el mensaje");
            }
        }

    }

}
