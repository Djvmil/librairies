package com.stepstone.stepper.adapter;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Interface to be used as model to {@link com.stepstone.stepper.StepperLayout}.
 */
public interface StepAdapter {

    /**
     * Create each step of the {@link com.stepstone.stepper.StepperLayout}.
     * @param position The position of the {@link PagerAdapter} to be used inside the {@link androidx.viewpager.widget.ViewPager}.
     * @return the step to be used inside the {@link com.stepstone.stepper.StepperLayout}.
     */
    Step createStep(@IntRange(from = 0) int position);

    /**
     * Finds the given step without creating it.
     * @see androidx.fragment.app.FragmentPagerAdapter #makeFragmentName(int, long)
     * @param position step position
     * @return step fragment
     */
    Step findStep(@IntRange(from = 0) int position);

    /**
     * Returns the view information to be used to display the step.
     * @param position step position
     * @return view information
     */
    @NonNull
    StepViewModel getViewModel(@IntRange(from = 0) int position);

    /**
     * Get the step count.
     * @return the quantity of steps
     */
    @IntRange(from = 0)
    int getCount();

    /**
     * Method for internal purpose. Should not be inherited.
     * @return the adapter to be used in the {@link androidx.viewpager.widget.ViewPager}.
     */
    PagerAdapter getPagerAdapter();
}
