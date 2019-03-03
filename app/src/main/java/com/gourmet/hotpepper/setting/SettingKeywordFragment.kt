package com.gourmet.hotpepper.setting

import android.R
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.gourmet.hotpepper.Injection
import com.gourmet.hotpepper.shoplist.ShopListFragment
import com.gourmet.hotpepper.shoplist.ShopListPresenter
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class SettingKeywordFragment : Fragment() {

    private val KEY_LAT = "KEY_LAT"
    private val KEY_LNG = "KEY_LNG"
    private val KEYWORD_1 = "KEYWORD_1"
    private val KEYWORD_2 = "KEYWORD_2"
    private val KEYWORD_3 = "KEYWORD_3"

    private var latitude = 0.0
    private var longitude = 0.0
    private var word1 = ""
    private var word2 = ""
    private var word3 = ""

    private var checkedCount = 0
    private var checkedIndexArray: MutableList<Int> = mutableListOf()

    private lateinit var enterButton: Button
    private lateinit var keywordListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args == null) {

            latitude = 0.0
            longitude = 0.0
            word1 = ""
            word2 = ""
            word3 = ""
        } else {

            latitude = args.getDouble(KEY_LAT)
            longitude = args.getDouble(KEY_LNG)
            word1 = args.getString(KEYWORD_1)
            word2 = args.getString(KEYWORD_2)
            word3 = args.getString(KEYWORD_3)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(com.gourmet.hotpepper.R.menu.menu_setting_keyword_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
            }

            com.gourmet.hotpepper.R.id.clear_keyword -> {
                checkedCount = 0
                checkedIndexArray.clear()
                val checked = keywordListView.checkedItemPositions
                for (i in 0 until checked.size()) {
                    if (checked.valueAt(i)) {
                        Log.d("clear_keyword", "Takishtia checked index = " + i)
                        keywordListView.setItemChecked(i, false)
                    }
                }
                keywordListView.clearChoices()
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

        val arrayKeyword = resources.getStringArray(com.gourmet.hotpepper.R.array.keyword_string_array)
        val adapter = ArrayAdapter<String>(context, R.layout.simple_list_item_multiple_choice, arrayKeyword)
        val root = inflater.inflate(com.gourmet.hotpepper.R.layout.fragment_setting_keyword, container, false)

        val toolbar = root.findViewById(com.gourmet.hotpepper.R.id.stToolbar) as Toolbar
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        activity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        // キーワード一覧
        keywordListView = root.findViewById(com.gourmet.hotpepper.R.id.keyword_listView) as ListView
        keywordListView.adapter = adapter

        // 選択済みのキーワードはチェック済みにする
        setKeywordCheck()

        // ボタン押下時Listener
        setItemClickListener()

        // 決定ボタン
        enterButton = root.findViewById(com.gourmet.hotpepper.R.id.enter_btn)
        enterButton.setOnClickListener {

            var checkedArray: MutableList<String> = mutableListOf("", "", "")
            val arrayKeyword = resources.getStringArray(com.gourmet.hotpepper.R.array.keyword_string_array)
            var addIndex = 0
            for (i in 0 until arrayKeyword.size) {
                if (keywordListView.isItemChecked(i)) {
                    Log.d("enterButton", "Takishtia arrayKeyword[i] = " + arrayKeyword[i])
                    checkedArray[addIndex] = arrayKeyword[i]
                    addIndex++
                }
            }

            showShopListFragment(checkedArray)
        }

        return root
    }

    private fun showShopListFragment(checkedArray: MutableList<String>) {
        val fragmentManager = fragmentManager

        if (fragmentManager != null) {

            word1 = getKeyword(checkedArray[0])
            word2 = getKeyword(checkedArray[1])
            word3 = getKeyword(checkedArray[2])

            val shopListFragment = childFragmentManager
                .findFragmentById(com.gourmet.hotpepper.R.id.shopListContainer) as ShopListFragment?
                ?: ShopListFragment.newInstance(latitude, longitude, word1, word2, word3).also {
                    replaceFragmentInActivity(it, com.gourmet.hotpepper.R.id.shopListContainer)
                }

            ShopListPresenter(
                Injection.provideShopListRepository(context!!), shopListFragment)
        }
    }

    private fun getKeyword(keyword: String): String {

        var word = ""
        if (keyword.isNotEmpty()) {
            word = keyword
        }
        return word
    }

    private fun setKeywordCheck() {

        val arrayKeyword = resources.getStringArray(com.gourmet.hotpepper.R.array.keyword_string_array)
        for (i in 0 until arrayKeyword.size) {
            if (word1 == arrayKeyword[i] || word2 == arrayKeyword[i] || word3 == arrayKeyword[i]) {
                keywordListView.setItemChecked(i, true)
                checkedIndexArray.add(i)
                checkedCount++
            }
        }
        Log.d("setKeywordCheck", "Takishtia checkedIndexArray = " + checkedIndexArray)
        Log.d("setKeywordCheck", "Takishtia checkedCount = " + checkedCount)
    }

    private fun setItemClickListener() {

        keywordListView.setOnItemClickListener { adapterView, view, i, l ->

            Log.d("setOnItemClickListener", "Takishtia BEFORE checkedIndexArray = " + checkedIndexArray)
            Log.d("setOnItemClickListener", "Takishtia BEFORE i = " + i)
            // チェック済み項目が選ばれた場合
            if (checkedIndexArray.contains(i)) {

                val removeIndex = checkedIndexArray.indexOfFirst { it == i }
                checkedIndexArray.removeAt(removeIndex)
                checkedCount--

                Log.d("setOnItemClickListener", "Takishtia AFTER checkedIndexArray = " + checkedIndexArray)
                return@setOnItemClickListener
            }

            checkedCount++
            Log.d("setOnItemClickListener", "Takishtia checkedCount = " + checkedCount)
            // 3件チェックを超えた場合
            if (checkedCount > 3) {

                Log.d("setOnItemClickListener", "Takishtia checkedIndexArray[0] = " + checkedIndexArray[0])
                // IndexListを削除、checkedCountをデクリメント、チェックを外す
                checkedCount--
                keywordListView.setItemChecked(checkedIndexArray[0], false)
                checkedIndexArray.removeAt(0)
                Log.d("setOnItemClickListener", "Takishtia REMOVE AFTER checkedIndexArray = " + checkedIndexArray)
                checkedIndexArray.add(i)
            } else {

                checkedIndexArray.add(i)
            }
            Log.d("setOnItemClickListener", "Takishtia ADD AFTER checkedIndexArray = " + checkedIndexArray)
        }
    }

    private fun replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
        fragmentManager?.transact {
            replace(frameId, fragment)
        }
    }

    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.commit()
    }

    companion object {

        fun newInstance(lat: Double, lng: Double, keyword1: String, keyword2: String, keyword3: String) =
            SettingKeywordFragment().apply {
                arguments = Bundle().apply {

                    putDouble(KEY_LAT, lat)
                    putDouble(KEY_LNG, lng)
                    putString(KEYWORD_1, keyword1)
                    putString(KEYWORD_2, keyword2)
                    putString(KEYWORD_3, keyword3)
                }
            }
    }
}