package com.gourmet.hotpepper.shoplist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.gourmet.hotpepper.setting.SettingKeywordFragment
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class ShopListFragment : Fragment(), ShopListContract.View {

    enum class HotpepperMenu(val position: Int) {
        SETTING_KEYWORD(0)
    }

    override lateinit var presenter: ShopListContract.Presenter

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var editText: EditText
    private lateinit var searchButton: Button
    private lateinit var keywordButton1: Button
    private lateinit var keywordButton2: Button
    private lateinit var keywordButton3: Button

    private var drawerToggle: ActionBarDrawerToggle? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var freeword = "居酒屋"
    private var isPressedKeyword1 = false
    private var isPressedKeyword2 = false
    private var isPressedKeyword3 = false
    private var settingKeyword1 = ""
    private var settingKeyword2 = ""
    private var settingKeyword3 = ""
    private var keywordForButton = ""
    private var keywordArrayForButton: MutableList<String> = mutableListOf("", "", "")
    private lateinit var navigationListView: ListView

    private val KEY_LAT = "KEY_LAT"
    private val KEY_LNG = "KEY_LNG"
    private val SETTING_KEYWORD_1 = "SETTING_KEYWORD_1"
    private val SETTING_KEYWORD_2 = "SETTING_KEYWORD_2"
    private val SETTING_KEYWORD_3 = "SETTING_KEYWORD_3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args = arguments
        if (args == null) {

            latitude = 0.0
            longitude = 0.0
            settingKeyword1 = ""
            settingKeyword2 = ""
            settingKeyword3 = ""
        } else {

            latitude = args.getDouble(KEY_LAT)
            longitude = args.getDouble(KEY_LNG)
            settingKeyword1 = args.getString(SETTING_KEYWORD_1)
            settingKeyword2 = args.getString(SETTING_KEYWORD_2)
            settingKeyword3 = args.getString(SETTING_KEYWORD_3)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(com.gourmet.hotpepper.R.menu.menu_shoplist_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            com.gourmet.hotpepper.R.id.reset_info -> {
                reset()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setFontAttrId(com.gourmet.hotpepper.R.attr.fontPath)
                .build()
        )

        container?.removeAllViews()
        var root = inflater.inflate(com.gourmet.hotpepper.R.layout.fragment_shoplist, container, false)

        val toolbar = root.findViewById(com.gourmet.hotpepper.R.id.tbToolbar) as Toolbar
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)

        drawerLayout = root.findViewById(com.gourmet.hotpepper.R.id.hotpepper_drawer_layout) as DrawerLayout
        drawerToggle = ActionBarDrawerToggle(activity!!, drawerLayout, toolbar, com.gourmet.hotpepper.R.string.app_name, com.gourmet.hotpepper.R.string.app_name)
        drawerLayout.addDrawerListener(drawerToggle!!)
        drawerToggle!!.syncState()
        drawerToggle!!.isDrawerIndicatorEnabled = true

        // Drawer準備
        navigationListView = root.findViewById(com.gourmet.hotpepper.R.id.navigation_menu_list) as ListView
        createNavigationDrawer()

        recyclerView = root.findViewById(com.gourmet.hotpepper.R.id.recycler_view)
        editText = root.findViewById(com.gourmet.hotpepper.R.id.search_edit_text)
        editText.setOnKeyListener { view, i, keyEvent ->
            val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                //キーボードを閉じる
                inputMethodManager.hideSoftInputFromWindow(
                    editText.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
            }
            return@setOnKeyListener false
        }

        searchButton = root.findViewById(com.gourmet.hotpepper.R.id.search_button)
        searchButton.setOnClickListener {

            //キーボードを閉じる
            val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)

            val editText = root.findViewById<EditText>(com.gourmet.hotpepper.R.id.search_edit_text)
            if (editText.text.toString().isEmpty()) {
                showToastNotFreeword()
            }

            freeword = editText.text.toString()
            searchFromFreeWord()
        }

        // キーワード①の設定
        keywordButton1 = root.findViewById(com.gourmet.hotpepper.R.id.keyword_btn_1)
        keywordButton1.text = settingKeyword1
        keywordButton1.setOnClickListener {

            if (keywordButton1.text.toString().isEmpty()) {
                showToastSettingKeyword()
                return@setOnClickListener
            }

            if (isPressedKeyword1) {

                unsetKeyword(keywordButton1, 0)
                isPressedKeyword1 = false
            } else {

                setKeyword(keywordButton1, keywordButton1.text.toString(), 0)
                isPressedKeyword1 = true
            }

            searchFromKeywordButton()
        }

        // キーワード②の設定
        keywordButton2 = root.findViewById(com.gourmet.hotpepper.R.id.keyword_btn_2)
        keywordButton2.text = settingKeyword2
        keywordButton2.setOnClickListener {

            if (keywordButton2.text.toString().isEmpty()) {
                showToastSettingKeyword()
                return@setOnClickListener
            }

            if (isPressedKeyword2) {

                unsetKeyword(keywordButton2, 1)
                isPressedKeyword2 = false
            } else {

                setKeyword(keywordButton2, keywordButton2.text.toString(), 1)
                isPressedKeyword2 = true
            }

            searchFromKeywordButton()
        }

        // キーワード③の設定
        keywordButton3 = root.findViewById(com.gourmet.hotpepper.R.id.keyword_btn_3)
        keywordButton3.text = settingKeyword3
        keywordButton3.setOnClickListener {

            if (keywordButton3.text.toString().isEmpty()) {
                showToastSettingKeyword()
                return@setOnClickListener
            }

            if (isPressedKeyword3) {

                unsetKeyword(keywordButton3, 2)
                isPressedKeyword3 = false
            } else {

                setKeyword(keywordButton3, keywordButton3.text.toString(), 2)
                isPressedKeyword3 = true
            }

            searchFromKeywordButton()
        }
        return root
    }

    override fun showShopList(shopList: MutableList<ShopInfo>) {

        if (shopList.isEmpty()) {
            val msg = getString(com.gourmet.hotpepper.R.string.shop_not_found)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerViewAdapter = RecyclerViewAdapter(shopList, object : ShopListListener.ShopListClickListener {
            override fun onClickRow(shopUrl: String?) {

                val uri = Uri.parse(shopUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        })

        recyclerView!!.adapter = recyclerViewAdapter
        recyclerView!!.adapter.notifyDataSetChanged()
    }

    private fun searchFromKeywordButton() {

        if (keywordForButton.isNotEmpty()) {
            presenter.searchShopListByCurrentLocation(keywordForButton, latitude, longitude)
        }
    }

    private fun searchFromFreeWord() {

        presenter.searchShopList(freeword)
    }

    private fun setKeyword(keywordButton: Button, targetWord: String, index: Int) {

        keywordArrayForButton[index] = targetWord

        val buffer = StringBuilder()
        keywordArrayForButton.forEach {
            buffer.append(it)
            if (it.isNotEmpty()) {
                buffer.append(" ")
            }
        }

        keywordForButton = buffer.toString()
        Log.d("Takishtia setKeyword", "keywordForButton = " + keywordForButton)
        keywordButton.setBackgroundResource(com.gourmet.hotpepper.R.drawable.on_shape_rounded_corners)
    }

    private fun unsetKeyword(keywordButton: Button, index: Int) {

        keywordArrayForButton[index] = ""

        val buffer = StringBuilder()
        keywordArrayForButton.forEach {
            buffer.append(it)
            if (it.isNotEmpty()) {
                buffer.append(" ")
            }
        }

        keywordForButton = buffer.toString()
        Log.d("Takishtia unsetKeyword", "keywordForButton = " + keywordForButton)
        keywordButton.setBackgroundResource(com.gourmet.hotpepper.R.drawable.shape_rounded_corners)
    }

    private fun createNavigationDrawer() {
        val strings = resources.getStringArray(com.gourmet.hotpepper.R.array.menu_string_array)
        val adapter = ArrayAdapter<String>(context, com.gourmet.hotpepper.R.layout.menu_row, strings)
        navigationListView.adapter = adapter
        navigationListView.setOnItemClickListener { _, _, position, _ ->

            when (position) {
                HotpepperMenu.SETTING_KEYWORD.position -> showSettingKeywordFragment()
            }

            drawerLayout.closeDrawer(Gravity.START, true)
        }
    }

    private fun showSettingKeywordFragment() {
        val fragmentManager = fragmentManager

        if (fragmentManager != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            // BackStackを設定
            fragmentTransaction.addToBackStack(null)

            val keyword1 = keywordButton1.text.toString()
            val keyword2 = keywordButton2.text.toString()
            val keyword3 = keywordButton3.text.toString()
            fragmentTransaction.replace(
                com.gourmet.hotpepper.R.id.shopListContainer,
                SettingKeywordFragment.newInstance(latitude, longitude, keyword1, keyword2, keyword3)
            )
            fragmentTransaction.commit()
        }
    }

    private fun showToastNotFreeword() {
        val msg = getString(com.gourmet.hotpepper.R.string.ask_freeword)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showToastSettingKeyword() {
        val msg = getString(com.gourmet.hotpepper.R.string.ask_setting_keyword)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun reset() {
        isPressedKeyword1 = false
        isPressedKeyword2 = false
        isPressedKeyword3 = false
        keywordForButton = ""
        freeword = "居酒屋"

        editText.editableText.clear()
        keywordButton1.setBackgroundResource(com.gourmet.hotpepper.R.drawable.shape_rounded_corners)
        keywordButton2.setBackgroundResource(com.gourmet.hotpepper.R.drawable.shape_rounded_corners)
        keywordButton3.setBackgroundResource(com.gourmet.hotpepper.R.drawable.shape_rounded_corners)
    }

    companion object {

        fun newInstance(lat: Double, lng: Double, word1: String, word2: String, word3: String) =
            ShopListFragment().apply {
                arguments = Bundle().apply {

                    putDouble(KEY_LAT, lat)
                    putDouble(KEY_LNG, lng)
                    putString(SETTING_KEYWORD_1, word1)
                    putString(SETTING_KEYWORD_2, word2)
                    putString(SETTING_KEYWORD_3, word3)
                }
            }
    }
}