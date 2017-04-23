package com.medico.view.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.medico.application.MyApi;
import com.medico.util.ServerConnectionAdapter;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 06-04-2017.
 */

public class ParentActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener
{

    private List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
    private Document document;
    private int backStakeCount = 0;
    public MyApi api;
//    public int identifier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        document = getDocument();
        getFragmentManager().addOnBackStackChangedListener(this);
        api = ServerConnectionAdapter.getServerAdapter(this).getServerAPI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
//                if (fragmentList.size() > 0) {
                    onBackPressed();
                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    public void goHome()
    {
        super.onBackPressed();
    }
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 1 )
        {
            FragmentManager manager = getFragmentManager();
            manager.popBackStackImmediate();
            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount()-1);
            String name = entry.getName();
            if(name != null)
            {
                Fragment fragment = manager.findFragmentByTag(name);
                fragment.onStart();
            }
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
//        if(fragmentList.size()>0)
//            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
//        fragmentList.add(fragment);
    }
    public void detachFragment(ParentFragment fragment)
    {
//        if(fragmentList.size()>0)
//            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
//        fragmentList.remove(fragment);
    }
    public Fragment getParentFragment()
    {
        if(fragmentList.size()>0)
            return fragmentList.get(fragmentList.size()-1);
        else
            return null;
    }
//    private Document getDocument()
//    {
//        Document doc = null;
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        try
//        {
//            InputStream raw = getResources().openRawResource(R.raw.navigation);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            int i;
//            try
//            {
//                i = raw.read();
//                while (i != -1)
//                {
//                    byteArrayOutputStream.write(i);
//                    i = raw.read();
//                }
//                raw.close();
//            }
//            catch (IOException e)
//            {
//                // TODO Auto-generated catch block
//
//                e.printStackTrace();
//            }
//
//            String string = byteArrayOutputStream.toString();
//
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            InputSource is = new InputSource();
//            is.setCharacterStream(new StringReader(string));
//            doc = db.parse(is);
//
//        } catch (ParserConfigurationException e) {
//            Log.e("Error: ", e.getMessage());
//            return null;
//        } catch (SAXException e) {
//            Log.e("Error: ", e.getMessage());
//            return null;
//        } catch (IOException e) {
//            Log.e("Error: ", e.getMessage());
//            return null;
//        }
//        // return DOM
//        return doc;
//    }


    public void onBackStackChanged()
    {
//        FragmentManager manager = getFragmentManager();
//        if(manager.getBackStackEntryCount() > 1 && manager.getBackStackEntryCount() > backStakeCount)
//        {
//            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 2);
//            String name = entry.getName();
//            if (name != null)
//            {
//                Fragment fragment = manager.findFragmentByTag(name);
//                fragment.onPause();
//            }
//        }
//        if(manager.getBackStackEntryCount() > 0 && manager.getBackStackEntryCount() < backStakeCount)
//        {
//            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
//            String name = entry.getName();
//            if (name != null)
//            {
//                Fragment fragment = manager.findFragmentByTag(name);
//                fragment.onStart();
//            }
//        }
//        backStakeCount = manager.getBackStackEntryCount();
    }

}
