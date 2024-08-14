package io.github.samsalmag.foodbankandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.FragmentDishesBinding

class DishesFragment : Fragment() {

    private var _binding: FragmentDishesBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDishesBinding.inflate(layoutInflater, container, false)

        initButtonAddNewDish()

        return binding.root
    }

    private fun initButtonAddNewDish() {
        val buttonAddNewDish = binding.buttonNewDishView
        buttonAddNewDish.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AddDishFragment()).addToBackStack(null).commit()
        }
    }
}
