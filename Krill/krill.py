import matplotlib.pyplot as plt


time=0      # in years
deltaT= 0.0001  # arbitrary, should just be sufficiently small for accuracy
nSteps = 600000

krill=int(input("Enter initial number of krill (e.g. 700000): "))
whale=int(input("Enter initial number of whales (e.g. 3000): "))

# a,b,m,n are parameters in the model
a=float(input("Enter initial value for a (e.g. 0.2): "))
b=float(input("Enter initial value for b (e.g. 0.0001): "))
m=float(input("Enter initial value for m (e.g. 0.5): "))
n=float(input("Enter initial value for n (e.g. 0.000001): "))

timeList = [0]
krillList = [krill]
whaleList = [whale]

for i in range(nSteps):
	# time, krill and whale are updated from current values to next values
	# the derivative(=rate of change) for each quantity determines what happens!

	time = time + deltaT  # time always increased with deltaT
	
	oldKrill = krill
	krill = krill +  (a-b*whale)*krill    * deltaT
	whale = whale +  (-m+n*oldKrill)*whale   * deltaT
		
	timeList.append(time)
	krillList.append(krill)
	whaleList.append(whale)

# Maximum values for axes
max_k = 1000000
max_w = 10000

plt.figure(1)
plt.subplot(221)
plt.title('krill(t)')
plt.plot(timeList,krillList)
plt.axis([0, nSteps * deltaT, 0, max_k])

plt.subplot(222)
plt.title('whale(t)')
plt.plot(timeList,whaleList)
plt.axis([0, nSteps * deltaT, 0, max_w])

plt.subplot(223)
plt.title('parametric plot - whales and krill')
plt.plot(whaleList,krillList)
plt.axis([0,max_w,0,max_k])
plt.tight_layout()
plt.show()
