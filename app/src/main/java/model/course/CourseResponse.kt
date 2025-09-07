package model.course

data class CourseResponse(
    val resultVal: Int,
    val resultMsg: String,
    val careerPathDtos: List<Course>
)
