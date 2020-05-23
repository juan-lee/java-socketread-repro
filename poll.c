#define _GNU_SOURCE
#include <poll.h>
#include <dlfcn.h>
#include <stdio.h>

int poll(struct pollfd *fds, nfds_t nfds, int timeout) {
    static int n;
    if ((++n & 0x3) == 0) {
        // Fault injection: perhaps we should report a spurious readiness notification
        int i;
        for (i = 0; i < nfds; ++i) {
            if (fds[i].events & POLLIN) {
                fds[i].revents |= POLLIN;
                return 1;
            }
        }
    }
    return ((int(*)(struct pollfd*,nfds_t,int))dlsym(RTLD_NEXT, "poll"))(fds, nfds, timeout);
}
