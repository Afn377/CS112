# Given array of students and marks

arr = [
    [10, 20, 30, 40],
    [30, 34, 33, 20],
    [23, 43, 12, 32],
    [43, 12, 40, 30],
    [34, 23, 43, 23],
]

student_average = []
exam_average = [0,0,0,0]

for i in range(len(arr)):
    student_total = 0
    exam = 0
    for j in range(len(arr[i])):
        student_total += arr[i][j]
        exam_average[exam] += arr[i][j] / 5
        exam += 1
    student_average.append(student_total / len(arr[i]))


print("Student Average:", student_average)
print("Exam Average:", exam_average)