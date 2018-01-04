package org.openauto.unleashedaudioplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int audiodata = mediaCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

            // Store the title, id and artist name in Song Array list.
            do
            {
                long thisId = mediaCursor.getLong(idColumn);
                long thisalbumId = mediaCursor.getLong(albumId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisData = mediaCursor.getString(audiodata);
                String thisFileExt = getFileExt(thisData);

                // Add the info to our array.
                if(albumIdToQuery == thisalbumId)
                {
                    TrackModel tm = new TrackModel();
                    tm.setId(thisId);
                    tm.setTitle(thisTitle);
                    tm.setArtist(thisArtist);
                    tm.setData(thisData);
                    tm.setCoverArt(albumToLoad.getArt());
                    tm.setFileExt(thisFileExt);
                    tracks.add(tm);
                }
            }
            while (mediaCursor.moveToNext());

            // For best practices, close the cursor after use.
            mediaCursor.close();
        }
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

                albumData
                        .setId(cursor.getLong(cursor.getColumnIndex(_id)));

                albumData.setName(cursor.getString(cursor
                        .getColumnIndex(album_name)));

                albumData.setArtist(cursor.getString(cursor
                        .getColumnIndex(artist)));

                albumData.setArt(cursor.getString(cursor
                        .getColumnIndex(albumart)));

                albumData.setTracks(cursor.getString(cursor
                        .getColumnIndex(tracks)));

                list.add(albumData);

            } while (cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(list);
        return list;
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
