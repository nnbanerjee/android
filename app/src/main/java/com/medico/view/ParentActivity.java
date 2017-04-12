package com.medico.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.medico.application.R;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Narendra on 06-04-2017.
 */

public class ParentActivity extends AppCompatActivity
{

    private List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
    private Document document;
//    public int identifier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        document = getDocument();
//        LocationService locationService = LocationService.getLocationManager(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                if (fragmentList.size() > 1) {
                    onBackPressed();
                    return true;
                }
                else
                {
                    return false;
                }


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 1 )
        {
            FragmentManager manager = getFragmentManager();
            manager.popBackStack();
        }
        else
            super.onBackPressed();

//        if(getFragmentManager().getBackStackEntryCount() != fragmentList.size())
//            throw new RuntimeException("mismatch between fragment manager and fragment list");
//        if(getFragmentManager().getBackStackEntryCount() > 0 && fragmentList.size() > 0 )
//        {
//            FragmentManager manager = getFragmentManager();
//            Fragment fragment = fragmentList.get(fragmentList.size()-1);
//            String className = fragment.getClass().getName();
//            NodeList nodeList = document.getElementsByTagName(className.substring(className.lastIndexOf('.') + 1));
//            for(int i = 0; i < nodeList.getLength();i++) {
//                NamedNodeMap map = nodeList.item(i).getAttributes();
//                if(map == null || map.getLength() == 0) {
//                    manager.popBackStack();
//                    fragment.setHasOptionsMenu(false);
//                    fragmentList.remove(fragment);
//                    fragment = fragmentList.get(fragmentList.size() - 1);
//                    fragment.setHasOptionsMenu(true);
//                    fragment.onStart();
//                }
//                else
//                {
//                    if(getFragmentManager().getBackStackEntryCount() > 1 && fragmentList.size() > 1) {
//                        manager.popBackStack();
//                        manager.popBackStack();
//                        fragment.setHasOptionsMenu(false);
//                        fragmentList.remove(fragment);
//                        fragment = fragmentList.get(fragmentList.size() - 1);
//                        fragment.setHasOptionsMenu(true);
//                        fragmentList.remove(fragment);
//                        fragment = fragmentList.get(fragmentList.size() - 1);
//                        fragment.setHasOptionsMenu(true);
//                        fragment.onStart();
//                    }
//                    else
//                        throw new RuntimeException("Mismatch in fragment list and xml");
//                }
//            }
//
//        }
//        else
//            super.onBackPressed();
    }

    public void attachFragment(ParentFragment fragment)
    {
        if(fragmentList.size()>0)
            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
        fragmentList.add(fragment);
    }
    public void detachFragment(ParentFragment fragment)
    {
        if(fragmentList.size()>0)
            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
        fragmentList.remove(fragment);
    }
    public Fragment getParentFragment()
    {
        if(fragmentList.size()>0)
            return fragmentList.get(fragmentList.size()-1);
        else
            return null;
    }
    private Document getDocument()
    {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            InputStream raw = getResources().openRawResource(R.raw.navigation);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i;
            try
            {
                i = raw.read();
                while (i != -1)
                {
                    byteArrayOutputStream.write(i);
                    i = raw.read();
                }
                raw.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }

            String string = byteArrayOutputStream.toString();

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(string));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }
}
