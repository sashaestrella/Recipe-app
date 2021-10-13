package com.example.recipeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithme.recipeapp.R
import com.example.recipeapp.adapter.MainCategoryAdapter
import com.example.recipeapp.adapter.SubCategoryAdapter
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.entities.CategoryItems
import com.example.recipeapp.entities.MealsItems
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {
    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<MealsItems>()

    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                    arguments = Bundle().apply {
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromDb()

        mainCategoryAdapter.setClickListener(onCLicked)
        subCategoryAdapter.setClickListener(onCLickedSubItem)

        search_view.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr = ArrayList<MealsItems>()

                for (arr in arrSubCategory){
                    if (arr.strMeal!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                subCategoryAdapter.setData(tempArr)
                subCategoryAdapter.notifyDataSetChanged()
                return true
            }

        })
    }

    private fun getDataFromDb(){
        launch {
            context?.let {
                var cat = RecipeDatabase.getDatabase(it).recipeDao().getAllCategory()
                arrMainCategory = cat as ArrayList<CategoryItems>
                arrMainCategory.reverse()

                getMealDataFromDb(arrMainCategory[0].strcategory)
                mainCategoryAdapter.setData(arrMainCategory)
                rv_main_category.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL,false)
                rv_main_category.adapter = mainCategoryAdapter
            }
        }
    }

    private fun getMealDataFromDb(categoryName: String){
        tvCategory.text = "$categoryName Category"
        launch {
            context?.let {
                var cat = RecipeDatabase.getDatabase(it).recipeDao().getSpecificMealList(categoryName)
                arrSubCategory = cat as ArrayList<MealsItems>
                subCategoryAdapter.setData(arrSubCategory)
                rv_sub_category.layoutManager = LinearLayoutManager(it,
                    LinearLayoutManager.HORIZONTAL,false)
                rv_sub_category.adapter = subCategoryAdapter
            }
        }
    }

    private val onCLicked  = object : MainCategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }
    }


    private val onCLickedSubItem  = object : SubCategoryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var bundle = Bundle()
            bundle.putString("id", id)
            var fragment: Fragment = DetailFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment,false)
        }
    }

    fun replaceFragment(fragment: Fragment, istransition:Boolean){
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout, fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }

}