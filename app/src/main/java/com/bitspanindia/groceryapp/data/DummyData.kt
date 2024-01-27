package com.bitspanindia.groceryapp.data

import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.MainCategoryData
import com.bitspanindia.groceryapp.data.model.SliderModel

object DummyData {

        val data = listOf(
            SliderModel(
                name = "Uncle Chips",
                quantity = "2 Pieces",
                offPrice = "₹0",
                price = "₹40",
                image= R.drawable.uncle_chips
            ),
            SliderModel(
                name = "Bingo Tedhe Medhe",
                quantity = "90 g",
                offPrice = "₹60",
                price = "₹42",
                image= R.drawable.tedhe_medhe
            ),
            SliderModel(
                name = "Kurkure",
                quantity = "82 g",
                offPrice = "₹60",
                price = "₹54",
                image= R.drawable.kukure
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays1
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.kaccha_mango_bite
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays_american
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.kaccha_mango_bite1
            ),
            SliderModel(
                name = "Lay's  sdsd sds sd American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays1
            ),

            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays1
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.kaccha_mango_bite
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays_american
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.kaccha_mango_bite1
            ),
        ) // Replace this with your data

        val bannerData = listOf(R.drawable.banner_1, R.drawable.banner_2,
            R.drawable.banner_3, R.drawable.banner_4,  R.drawable.banner_2,
            R.drawable.banner_3, R.drawable.banner_1)


        val mainCategory = listOf<MainCategoryData>(
            MainCategoryData(R.drawable.image_vegetable_cat, "Fruit & Vegetable"),
            MainCategoryData(R.drawable.image_bakery_cat, "Cake & Bakery"),
            MainCategoryData(R.drawable.image_meat_cat, "Fish & Meat"),
            MainCategoryData(R.drawable.image_vegetable_cat, "Fruit & Vegetable"),
            MainCategoryData(R.drawable.image_kitchen_oils_cat, "Kitchen Oils"),
            MainCategoryData(R.drawable.image_meat_cat, "Fish & Meat"),
            MainCategoryData(R.drawable.image_bakery_cat, "Cake & Bakery"),
            MainCategoryData(R.drawable.image_vegetable_cat, "Fruit & Vegetable"),
            MainCategoryData(R.drawable.image_kitchen_oils_cat, "Kitchen Oils"),
            MainCategoryData(R.drawable.image_meat_cat, "Fish & Meat"),
            MainCategoryData(R.drawable.image_bakery_cat, "Cake & Bakery"),
            MainCategoryData(R.drawable.image_meat_cat, "Fish & Meat")
        )

}