package es.unavarra.tlm.prueba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

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

import es.unavarra.tlm.prueba.PantallaPrincipal.UsuarioRegistrado;

/**
 * Created by ibai on 10/20/17.
 */

public class ClasePeticionRest {

    //public ArrayList<KeyValue> respuesta = new ArrayList<>();

    public ClasePeticionRest(){
        super();
    }

    public static ArrayList<KeyValue> peticionRest(final ArrayList<KeyValue> parametros, final String funcionAPI, final String metodo){

        final ArrayList<KeyValue> respuesta = new ArrayList<>();
        //final int[] responseCode = {-1};


        try {

            respuesta.clear();

            String urlParametros = URLEncoder.encode(parametros.get(0).getKey(), "utf-8")+"="+URLEncoder.encode(parametros.get(0).getValue(), "utf-8");
            for (int x = 1; x < parametros.size(); x++){
                urlParametros += "&"+URLEncoder.encode(parametros.get(x).getKey(), "utf-8")+"="+URLEncoder.encode(parametros.get(x).getValue(), "utf-8");
            }

            String stringURL = "http://swappie.tk/base/php/" + funcionAPI + ".php?"+urlParametros;
            URL url = new URL(stringURL);
            //Log.e("URL", url.toString());
            //Log.d("etiqueta", String.valueOf(url));


            final HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
            myConnection.setInstanceFollowRedirects(false);

            if (metodo.equals("post")){
                myConnection.setDoOutput(true);
            }
            for (int x = 0; x < parametros.size(); x++){
                myConnection.setRequestProperty(parametros.get(x).getKey(), parametros.get(x).getValue());
            }


           /*AsyncTask.execute(new Runnable() {

                public void run() {
                    try {
                        responseCode[0] = myConnection.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });


            SystemClock.sleep(3000);



            if (responseCode[0] == 200) {
            */


            if (myConnection.getResponseCode() == 200){

                Log.d("probando1", "entro al if");
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.setLenient(true);
                jsonReader.beginObject(); // Start processi ng the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    String value = jsonReader.nextString();

                    respuesta.add(new KeyValue(key, value));
                }
                jsonReader.close();


            } else {
                Log.d("probando1", "entro al else");
                respuesta.add(new KeyValue("ok", "false"));
                respuesta.add(new KeyValue("error", "error en la peticion"));
            }


            myConnection.disconnect();




        } catch (Exception e) {
            e.printStackTrace();

        }

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
        String urlString = "http://swappie.tk/base/php/guardar_foto.php";

        try {

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

            while ((str = inStream.readLine()) != null) {

                Log.e("Debug", "Server Response " + str);

            }

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

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;


        public GuardarUsuario(Context context, String nombre, String apellidos, String email, String password, String ubicacion, String metodoLogin) {
            this.context = context;
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

        }



        @Override
        protected Integer doInBackground(String... strings) {


            final  ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");

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
            guardarUsuarioEnSharedPreferences(result);
            Toast.makeText(context, "Creado usuario Nº " + result, Toast.LENGTH_SHORT).show();
        }

        public void guardarUsuarioEnSharedPreferences(int id){

            SharedPreferences settings = context.getSharedPreferences("Config", 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("user", id);
            editor.putString("metodo", metodoLogin);
            editor.putBoolean("sesion", true);
            editor.putString("nombre",nombre);
            editor.putString("apellidos",apellidos);
            editor.putString("email",email);

            editor.commit();

            Intent intent = new Intent(context, UsuarioRegistrado.class);
            context.startActivity(intent);

        }

    }

    public static class GuardarSwipe extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_swipe";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarSwipe(Context context, int idUsuario, int idObjeto, boolean decision) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario", idUsuario+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            parametros.add(new KeyValue("decision", decision+""));
            this.context = context;
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
                Toast.makeText(context, "Swipe guardado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al guardar el swipe", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class GuardarMatch extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_match";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarMatch(Context context, int idUsuario1, int idUsuario2, boolean chatEmpezado) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_usuario2", idUsuario2+""));
            parametros.add(new KeyValue("chat_empezado", chatEmpezado+""));
            this.context = context;
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
                Toast.makeText(context, "Match guardado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al guardar el match", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class CrearChat extends AsyncTask<String, String, String> {

        String funcionAPI = "crear_chat";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public CrearChat(Context context, int idUsuario1, int idUsuario2) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_usuario2", idUsuario2+""));
            this.context = context;
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
                Toast.makeText(context, "Chat creado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al crear el chat", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class GuardarMensaje extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_mensaje";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarMensaje(Context context, int idChat, int idAutor, String mensaje) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_chat", idChat+""));
            parametros.add(new KeyValue("id_autor", idAutor+""));
            parametros.add(new KeyValue("mensaje", mensaje));
            this.context = context;
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
                Toast.makeText(context, "Mensaje guardado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al guardar el mensaje", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class GuardarObjeto extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_objeto";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarObjeto(Context context, int idUsuario, int idObjeto, String descripcion) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario", idUsuario+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            parametros.add(new KeyValue("descripcion", descripcion));
            this.context = context;
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
                Toast.makeText(context, "Objeto guardado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al guardar el objeto", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class GuardarFoto extends AsyncTask<String, String, Integer> {

        String funcionAPI = "guardar_objeto";
        File foto;

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarFoto(Context context, File foto) {
            this.context = context;
            this.foto = foto;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = doFileUpload(foto);
            int idObjeto = 0;
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                if (respuesta.get(1).getKey().equals("id_objeto")){
                    idObjeto = Integer.parseInt(respuesta.get(1).getValue());
                }
            }
            return idObjeto;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                Toast.makeText(context, "Foto del Objeto Nº "+ result +" guardada correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al crear el objeto", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class ComprobarSwipe extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_swipe";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public ComprobarSwipe(Context context, int idUsuario1, int idObjeto) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.context = context;
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
                    Toast.makeText(context, "MATCH!", Toast.LENGTH_SHORT).show();
                }else if (result.get(1).getValue().equals("false")){
                    Toast.makeText(context, "NO MATCH!", Toast.LENGTH_SHORT).show();
                }
            }else if (result.get(1).getKey().equals("error")){
                Toast.makeText(context, "ERROR: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }

            if (result.equals("true")){
                Toast.makeText(context, "Objeto guardado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error al guardar el objeto", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class CogerSwipes extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_swipes";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public CogerSwipes(Context context, int idUsuario1) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            this.context = context;
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
                Toast.makeText(context, "JSON: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }else if (result.get(1).getKey().equals("error")){
                Toast.makeText(context, "JSON: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public static class CogerInfoObjeto extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_info_objeto";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public CogerInfoObjeto(Context context, int idObjeto) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.context = context;
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
                Toast.makeText(context, "JSON: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }else if (result.get(1).getKey().equals("error")){
                Toast.makeText(context, "JSON: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public static class ActualizarChat extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "actualizar_chat";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public ActualizarChat(Context context, int idUsuario, int idChat) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("id_usuario", idUsuario + ""));
            parametros.add(new KeyValue("id_chat", idChat + ""));
            this.context = context;
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
                Toast.makeText(context, "JSON: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }else if (result.get(1).getKey().equals("error")){
                Toast.makeText(context, "JSON: " + result.get(1).getValue(), Toast.LENGTH_SHORT).show();
            }

        }

    }

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



}