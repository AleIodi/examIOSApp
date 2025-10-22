
//
//  Credenziali.swift
//  prove
//
//  Created by d4 on 17/10/25.
//
import Foundation

struct Credenziali {
    var username = ""
    var password = ""
    var email = ""
    var birthDate: Date?
    
    func usernameError() -> String? {
        if username.isEmpty { return nil }
        if username.count < 3 {
            return "Username must be at least 3 characters long."
        }
        return nil
    }
    
    func passwordError() -> String? {
        if password.isEmpty { return nil }
        if password.count < 8 {
            return "Password must be at least 8 characters long."
        }
        
        var hasUppercase = false
        var hasLowercase = false
        var hasDigit = false
        var hasSpecialChar = false
        
        let specialCharacters = "!@#$%^&*()-_=+{}[]|:;\"'<>,.?/~`"
        
        for char in password {
            if char.isUppercase {
                hasUppercase = true
            } else if char.isLowercase {
                hasLowercase = true
            } else if char.isNumber {
                hasDigit = true
            } else if specialCharacters.contains(char) {
                hasSpecialChar = true
            }
        }
        
        var errorMessages: [String] = []
        if !hasUppercase { errorMessages.append("an uppercase letter") }
        if !hasLowercase { errorMessages.append("a lowercase letter") }
        if !hasDigit { errorMessages.append("a digit") }
        if !hasSpecialChar { errorMessages.append("a special character") }
        
        if !errorMessages.isEmpty {
            return "Password requires " + errorMessages.joined(separator: ", ") + "."
        }
        
        return nil
    }
    
    func emailError() -> String? {
        if email.isEmpty { return nil }
        
        if email.contains(" ") {
            return "Email cannot contain spaces."
        }
        
        let parts = email.split(separator: "@")
        
        if parts.count != 2 {
            return "Email must contain exactly one '@' symbol."
        }
        
        let namePart = parts[0]
        let domainPart = parts[1]
        
        if namePart.isEmpty || domainPart.isEmpty {
            return "Email must have a name and a domain part."
        }
        
        if !domainPart.contains(".") || domainPart.count < 3 {
            return "The email domain is not valid (e.g., example.com)."
        }
        
        return nil
    }
    
    func birthDateError(birthDate: Date) -> Bool {
        let calendar = Calendar.current
        let now = Date()
        
        let ageComponents = calendar.dateComponents([.year], from: birthDate, to: now)
        if ageComponents.year ?? 0 < 14 {
            return true
        }
        
        return false
    }
    
    
    func isValidForSignIn() -> Bool {
        !username.isEmpty && usernameError() == nil &&
        !password.isEmpty && passwordError() == nil
    }
    
    func isValidForSignUp() -> Bool {
        isValidForSignIn() &&
        !email.isEmpty && emailError() == nil
    }
    
    mutating func clear() {
        username = ""
        password = ""
        email = ""
    }
}
