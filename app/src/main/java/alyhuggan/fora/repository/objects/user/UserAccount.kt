package alyhuggan.fora.repository.objects.user

data class UserAccount(val userName: String = "", val email: String = "",
                       val recipeList: List<UserItems> = emptyList(),
                       val foodList: List<UserItems> = emptyList()) {
}