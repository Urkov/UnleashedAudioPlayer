package org.openauto.unleashedaudioplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.openauto.unleashedaudioplayer.entities.AlbumModel;
import org.openauto.unleashedaudioplayer.entities.TrackModel;
import org.openauto.unleashedaudioplayer.entities.WebradioModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaStoreHandler {

    public static List<TrackModel> getTracksForAlbum(Context context, AlbumModel albumToLoad){

        long albumIdToQuery = albumToLoad.getId();
        List<TrackModel> tracks = new ArrayList<>();

        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mediaCursor = context.getContentResolver().query(mediaUri, null, null, null, null);

        // if the cursor is null.
        if(mediaCursor != null && mediaCursor.moveToFirst())
        {
            //get Columns
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int trackColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int audiodata = mediaCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int year = mediaCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR);

            // Store the title, id and artist name in Song Array list.
            do
            {
                long thisId = mediaCursor.getLong(idColumn);
                long thisalbumId = mediaCursor.getLong(albumId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisData = mediaCursor.getString(audiodata);
                String thisTrack = mediaCursor.getString(trackColumn);
                String thisYear = mediaCursor.getString(year);
                String thisFileExt = getFileExt(thisData);

                // Add the info to our array.
                if(albumIdToQuery == thisalbumId)
                {
                    TrackModel tm = new TrackModel();
                    tm.setTrack(thisTrack);
                    tm.setAlbumId(thisalbumId);
                    tm.setId(thisId);
                    tm.setTitle(thisTitle);
                    tm.setArtist(thisArtist);
                    tm.setData(thisData);
                    tm.setYear(thisYear);
                    tm.setCoverArt(albumToLoad.getArt());
                    tm.setFileExt(thisFileExt);
                    tracks.add(tm);
                }
            }
            while (mediaCursor.moveToNext());

            // For best practices, close the cursor after use.
            mediaCursor.close();
        }

        Collections.sort(tracks);
        return tracks;
    }


    public static ArrayList<AlbumModel> getListOfAlbums(Context context) {

        String where = null;

        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
        final String album_name = MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String albumart = MediaStore.Audio.Albums.ALBUM_ART;
        final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

        final String[] columns = { _id, album_name, artist, albumart, tracks };
        Cursor cursor = context.getContentResolver().query(uri, columns, where,
                null, null);

        ArrayList<AlbumModel> list = new ArrayList<>();
        // add playlsit to list

        if (cursor.moveToFirst()) {

            do {

                AlbumModel albumData = new AlbumModel();
                albumData.setId(cursor.getLong(cursor.getColumnIndex(_id)));

                String nameStr = cursor.getString(cursor.getColumnIndex(album_name));
                String artistStr = cursor.getString(cursor.getColumnIndex(artist));
                //This is for creating a simple index letter for unknown artists and albums
                if(nameStr.startsWith("<unknown")){
                    nameStr = "⁇";
                }
                if(artistStr.startsWith("<unknown")){
                    artistStr = "⁇";
                }
                albumData.setName(nameStr);
                albumData.setArtist(artistStr);
                albumData.setArt(cursor.getString(cursor.getColumnIndex(albumart)));
                albumData.setTracks(cursor.getString(cursor.getColumnIndex(tracks)));
                list.add(albumData);

            } while (cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(list);
        return list;
    }


    public static ArrayList<WebradioModel> getWebradioList(Context context) {

        ArrayList<WebradioModel> list = null;

        //Try to read webradio information
        try {
            final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Radio/radio.txt");
            StringBuilder text = new StringBuilder();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();

            Gson gson = new Gson();
            list = gson.fromJson(text.toString(), new TypeToken<List<WebradioModel>>(){}.getType());
        }
        catch (Exception e) {
            //You'll need to add proper error handling here
        }

        if(list != null){
            Collections.sort(list);
            return list;
        } else {
            return new ArrayList<>();
        }

    }

    private static String getFileExt(String fileName){
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

}
