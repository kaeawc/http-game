package forms

case class Signup(
  email      : String,
  passwords  : (String,String),
  invitation : String
)