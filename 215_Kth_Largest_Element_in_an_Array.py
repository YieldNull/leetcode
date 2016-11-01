class Solution(object):
    def findKthLargest(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        # return self.find_recursion_loop(nums,k)
        return self.find_inplace(nums, k)

    def find_recursion(self, nums, k):
        pivot = nums[0]

        smaller = [x for x in nums[1:] if x <= pivot]
        greater = [x for x in nums[1:] if x > pivot]

        counter = len(greater) + 1

        if counter == k:
            return pivot
        elif counter > k:
            return self.find_recursion(greater, k)
        else:
            return self.find_recursion(smaller, k - counter)

    def find_recursion_loop(self, nums, k):
        while True:
            pivot = nums[0]

            smaller = [x for x in nums[1:] if x <= pivot]
            greater = [x for x in nums[1:] if x > pivot]

            counter = len(greater) + 1

            if counter == k:
                return pivot
            elif counter > k:
                nums = greater
            else:
                nums = smaller
                k -= counter

    def find_inplace(self, nums, k):
        return self.find_inplace_recursion_to_loop(nums, 0, len(nums) - 1, k)

    def find_inplace_recursion(self, nums, left, right, k):
        pivot = nums[left]

        p_pivot = self._inplace_partition(nums, left, right)

        counter = p_pivot - left + 1

        if counter == k:
            return pivot
        elif counter > k:
            return self.find_inplace_recursion(nums, left, p_pivot - 1, k)
        else:
            return self.find_inplace_recursion(nums, p_pivot + 1, right, k - counter)

    def find_inplace_recursion_to_loop(self, nums, left, right, k):
        while True:
            pivot = nums[left]

            p_pivot = self._inplace_partition(nums, left, right)

            counter = p_pivot - left + 1

            if counter == k:
                return pivot
            elif counter > k:
                right = p_pivot - 1
            else:
                left = p_pivot + 1
                k -= counter

    def _inplace_partition(self, nums, left, right):
        pivot = nums[left]

        p_left = left
        p_right = right + 1

        while p_left < p_right:
            p_left += 1
            p_right -= 1

            while p_left <= right and nums[p_left] > pivot:
                p_left += 1

            while p_right >= left and nums[p_right] < pivot:
                p_right -= 1

            if p_left < p_right:
                nums[p_left], nums[p_right] = nums[p_right], nums[p_left]

        nums[left], nums[p_right] = nums[p_right], nums[left]  # place pivot in the middle

        return p_right
