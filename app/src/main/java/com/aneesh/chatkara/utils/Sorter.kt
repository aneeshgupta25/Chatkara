package com.aneesh.chatkara.utils

import com.aneesh.chatkara.model.Restaurant

class Sorter {
    companion object{
        val costSorting = Comparator<Restaurant>{ restaurant1, restaurant2 ->
            val costOne = restaurant1.cost_for_one
            val costTwo = restaurant2.cost_for_one
            if(restaurant1.cost_for_one.compareTo(restaurant2.cost_for_one) == 0){
                restaurant1.cost_for_one.compareTo(restaurant2.cost_for_one)
            }else{
                costOne.compareTo(costTwo)
            }
        }
        val ratingSorting = Comparator<Restaurant>{ restaurant1, restaurant2 ->
            val costOne = restaurant1.cost_for_one
            val costTwo = restaurant2.cost_for_one
            if(restaurant1.rating.compareTo(restaurant2.rating) == 0){
                costOne.compareTo(costTwo)
            }else{
                restaurant1.rating.compareTo(restaurant2.rating)
            }
        }
    }
}