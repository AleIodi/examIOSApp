//  RegisterView.swift
//  prove
//
//  Created by d4 on 30/09/25.
//

        import SwiftUI

struct RegisterView: View {
    @State private var user = Credenziali()
    @State private var birthDate = Date()
    
    var body: some View {
        NavigationStack{
            VStack(spacing: 20) {
                Spacer()
                
                Image("Logo")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 150, height: 150)
                    .clipShape(RoundedRectangle(cornerRadius: 30))
                    .shadow(color: .black.opacity(0.1), radius: 10, y: 5)
                
                Text("Create Account")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                
                VStack(spacing: 15) {
                    CustomTextField(
                        fieldType: .email,
                        text: $user.email,
                        placeholder: "Email",
                        errorMessage: user.emailError()
                    )
                    
                    CustomTextField(
                        fieldType: .username,
                        text: $user.username,
                        placeholder: "Username",
                        errorMessage: user.usernameError()
                    )
                    
                    CustomTextField(
                        fieldType: .password,
                        text: $user.password,
                        placeholder: "Password",
                        errorMessage: user.passwordError()
                    )
                    
                    VStack(alignment: .leading){
                        HStack(spacing: 12) {
                            Image(systemName: "calendar")
                                .foregroundColor(user.birthDateError(birthDate: birthDate) ? .red : Color(.systemGray))
                                .frame(width: 20, alignment: .center)
                            
                            DatePicker(
                                "Date of Birth",
                                selection: $birthDate,
                                in: ...Date(),
                                displayedComponents: .date
                            )
                        }
                        .padding(15)
                        .background(Color.white)
                        .foregroundColor(user.birthDateError(birthDate: birthDate) ? .red : .black)
                        .cornerRadius(12)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(!user.birthDateError(birthDate: birthDate) ? Color(.systemGray4) : .red, lineWidth: 1.5)
                        )
                        .animation(.easeInOut(duration: 0.2), value: user.birthDateError(birthDate: birthDate))
                        
                        if user.birthDateError(birthDate: birthDate) {
                            Text("You must have at least 14 years.")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 10)
                                .transition(.opacity.animation(.easeIn))
                        }
                    }
                }
                .padding(.top)
                
                VStack(spacing: 12) {
                    CustomButton(
                        text: "Sign Up",
                        style: .primary,
                        isEnabled: user.isValidForSignUp(),
                        action: { print("Sign Up Tapped") }
                    )
                    
                    CustomButton(
                        text: "Clear",
                        style: .text,
                        action: { user.clear() }
                    )
                }
                .padding(.top)
                
                Spacer()
                
                VStack {
                    NavigationLink(destination: LoginView()) {
                        HStack(spacing: 4) {
                            Text("Already have an account?")
                            Text("Sign In")
                                .fontWeight(.semibold)
                        }
                        .font(.footnote)
                        .foregroundColor(Color(.systemGray))
                    }
                }
                .padding(.bottom, 20)
            }
            .padding(.horizontal, 30)
        }.navigationBarBackButtonHidden()
    }
}

#Preview {
    RegisterView()
}
