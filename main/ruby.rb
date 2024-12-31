n = 10
sum = 0
i = 1
while i <= n
    sum = sum + i
    i = i + 1
end

puts sum


t = 8
x = 120
k = 0
while k*t <= x
    k=k+1
end
puts k

n = 135187
reversed_number = 0
while n > 0
    last_digit = n % 10
    reversed_number = reversed_number * 10 + last_digit
    n = n/10
end
puts reversed_number




